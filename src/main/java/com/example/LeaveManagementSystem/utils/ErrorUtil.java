package com.example.LeaveManagementSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorUtil<T> {
    private Boolean ok;
    private T message;

    public Boolean isOk() {
        return this.ok;
    }
}
