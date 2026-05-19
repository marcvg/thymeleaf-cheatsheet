package com.example.cheatsheet.domain;

import java.time.LocalDate;

public record CreditDocument(String name, String type, LocalDate date) {}
