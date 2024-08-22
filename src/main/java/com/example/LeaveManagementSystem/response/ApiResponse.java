package com.example.LeaveManagementSystem.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    String message;
    Integer status;
    T data;
}
