package com.baopen753.securitywithauthorizationimplementing.model;

import org.springframework.http.HttpStatus;

public record LoginResponseDto(String status, String token) {
}
