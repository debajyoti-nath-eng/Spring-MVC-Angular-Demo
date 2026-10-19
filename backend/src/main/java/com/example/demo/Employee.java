package com.example.demo;

import java.math.BigDecimal;

public record Employee(
		long id,
		String firstName,
		String lastName,
		String email,
		String department,
		BigDecimal salary) {
}
