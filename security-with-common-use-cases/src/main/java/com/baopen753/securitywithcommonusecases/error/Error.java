package com.baopen753.securitywithcommonusecases.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
