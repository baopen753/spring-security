package com.baopen753.securitywithauthorizationimplementing.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Error {
    private String date;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
