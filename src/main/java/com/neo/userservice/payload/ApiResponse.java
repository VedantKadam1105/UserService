package com.neo.userservice.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Data
@Builder
public class ApiResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
}
