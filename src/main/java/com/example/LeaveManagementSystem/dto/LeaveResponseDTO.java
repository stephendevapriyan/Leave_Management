package com.example.LeaveManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveResponseDTO {

    private UUID id;
    private UUID employeeId;
    private Date startDate;
    private Date endDate;
    private String leaveType;
    private String status;
    private LocalDateTime requestDate;
    private String leaveReason;
    private LocalDateTime approvedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
