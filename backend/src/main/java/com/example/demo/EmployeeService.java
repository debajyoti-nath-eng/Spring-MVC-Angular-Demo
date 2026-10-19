package com.example.demo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeService {
	private final AtomicLong nextId = new AtomicLong(4);
	private final ConcurrentMap<Long, Employee> employees = new ConcurrentHashMap<>();

	public EmployeeService() {
		employees.put(1L, new Employee(1, "Avery", "Johnson", "avery.johnson@example.com", "Engineering",
				new BigDecimal("112000.00")));
		employees.put(2L, new Employee(2, "Morgan", "Lee", "morgan.lee@example.com", "Design",
				new BigDecimal("98000.00")));
		employees.put(3L, new Employee(3, "Jordan", "Patel", "jordan.patel@example.com", "Product",
				new BigDecimal("105000.00")));
	}

	public List<Employee> findAll() {
		return employees.values().stream()
				.sorted(Comparator.comparing(Employee::id))
				.toList();
	}

	public Employee findById(long id) {
		Employee employee = employees.get(id);
		if (employee == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
		}
		return employee;
	}

	public Employee create(EmployeeRequest request) {
		long id = nextId.getAndIncrement();
		Employee employee = toEmployee(id, request);
		employees.put(id, employee);
		return employee;
	}

	public Employee update(long id, EmployeeRequest request) {
		if (!employees.containsKey(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
		}
		Employee employee = toEmployee(id, request);
		employees.put(id, employee);
		return employee;
	}

	public void delete(long id) {
		if (employees.remove(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
		}
	}

	private Employee toEmployee(long id, EmployeeRequest request) {
		return new Employee(id, request.firstName().trim(), request.lastName().trim(), request.email().trim(),
				request.department().trim(), request.salary());
	}
}
