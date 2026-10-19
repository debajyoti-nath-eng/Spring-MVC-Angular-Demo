import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { Employee, EmployeeRequest, EmployeeService } from './employee.service';

@Component({
  imports: [CurrencyPipe, ReactiveFormsModule],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  private readonly formBuilder = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);

  protected readonly employees = signal<Employee[]>([]);
  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly error = signal('');
  protected readonly editingId = signal<number | null>(null);
  protected readonly employeeForm = this.formBuilder.nonNullable.group({
    firstName: ['', [Validators.required, Validators.maxLength(80)]],
    lastName: ['', [Validators.required, Validators.maxLength(80)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(160)]],
    department: ['', [Validators.required, Validators.maxLength(100)]],
    salary: [0, [Validators.required, Validators.min(0)]],
  });

  constructor() {
    void this.loadEmployees();
  }

  protected async loadEmployees(): Promise<void> {
    this.loading.set(true);
    this.error.set('');
    try {
      this.employees.set(await firstValueFrom(this.employeeService.findAll()));
    } catch (error: unknown) {
      this.error.set(this.describeError(error));
    } finally {
      this.loading.set(false);
    }
  }

  protected edit(employee: Employee): void {
    this.editingId.set(employee.id);
    this.employeeForm.setValue({
      firstName: employee.firstName,
      lastName: employee.lastName,
      email: employee.email,
      department: employee.department,
      salary: employee.salary,
    });
    this.error.set('');
  }

  protected cancelEdit(): void {
    this.editingId.set(null);
    this.employeeForm.reset({ firstName: '', lastName: '', email: '', department: '', salary: 0 });
    this.error.set('');
  }

  protected async saveEmployee(): Promise<void> {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.error.set('');
    const request: EmployeeRequest = this.employeeForm.getRawValue();
    try {
      const id = this.editingId();
      if (id === null) {
        await firstValueFrom(this.employeeService.create(request));
      } else {
        await firstValueFrom(this.employeeService.update(id, request));
      }
      this.cancelEdit();
      await this.loadEmployees();
    } catch (error: unknown) {
      this.error.set(this.describeError(error));
    } finally {
      this.saving.set(false);
    }
  }

  protected async deleteEmployee(employee: Employee): Promise<void> {
    if (!window.confirm(`Delete ${employee.firstName} ${employee.lastName}?`)) {
      return;
    }

    this.error.set('');
    try {
      await firstValueFrom(this.employeeService.delete(employee.id));
      if (this.editingId() === employee.id) {
        this.cancelEdit();
      }
      await this.loadEmployees();
    } catch (error: unknown) {
      this.error.set(this.describeError(error));
    }
  }

  protected fieldInvalid(field: 'firstName' | 'lastName' | 'email' | 'department' | 'salary'): boolean {
    const control = this.employeeForm.controls[field];
    return control.invalid && (control.dirty || control.touched);
  }

  private describeError(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      return error.status === 0
        ? 'Could not connect to the server. Make sure the Spring MVC API is running on port 8080.'
        : `Request failed (${error.status}): ${error.message}`;
    }
    return 'An unexpected error occurred. Please try again.';
  }
}
