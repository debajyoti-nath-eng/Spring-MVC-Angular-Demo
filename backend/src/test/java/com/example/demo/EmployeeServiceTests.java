package com.example.demo;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class EmployeeServiceTests {
	private final EmployeeService employeeService = new EmployeeService();

	@Test
	void createsUpdatesAndDeletesEmployees() {
		EmployeeRequest request = new EmployeeRequest(
				"Taylor", "Ng", "taylor.ng@example.com", "Operations", new BigDecimal("75000"));

		Employee created = employeeService.create(request);
		assertEquals(4, created.id());
		assertEquals(4, employeeService.findAll().size());

		EmployeeRequest updatedRequest = new EmployeeRequest(
				"Taylor", "Ng", "taylor.ng@example.com", "Finance", new BigDecimal("78000"));
		Employee updated = employeeService.update(created.id(), updatedRequest);
		assertEquals("Finance", updated.department());
		assertEquals(new BigDecimal("78000"), updated.salary());

		employeeService.delete(created.id());
		assertEquals(3, employeeService.findAll().size());
		assertThrows(ResponseStatusException.class, () -> employeeService.findById(created.id()));
	}

	@Test
	void rejectsUpdatesAndDeletesForMissingEmployees() {
		EmployeeRequest request = new EmployeeRequest(
				"Taylor", "Ng", "taylor.ng@example.com", "Operations", new BigDecimal("75000"));

		assertThrows(ResponseStatusException.class, () -> employeeService.update(999, request));
		assertThrows(ResponseStatusException.class, () -> employeeService.delete(999));
	}
}
