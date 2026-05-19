package com.example.cheatsheet.domain;

import java.time.LocalDate;

public record CreditTask(String description, LocalDate dueDate, String status) {}
