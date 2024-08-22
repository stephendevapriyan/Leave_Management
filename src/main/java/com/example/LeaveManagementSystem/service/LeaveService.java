package com.example.LeaveManagementSystem.service;

import com.example.LeaveManagementSystem.entity.EmployeeEntity;
import com.example.LeaveManagementSystem.entity.LeaveEntity;
import com.example.LeaveManagementSystem.entity.OrganizationEntity;
import com.example.LeaveManagementSystem.response.ApiResponse;

import java.util.UUID;

public interface LeaveService {

    public ApiResponse<OrganizationEntity> saveOrganization(OrganizationEntity oentity);
    public boolean organizationEmailExists(String email);
    public boolean checkLocation(String location);
    public ApiResponse<EmployeeEntity> saveEmployee(EmployeeEntity entity);
    public boolean isEmailExists(String email);
    public boolean isOrganizationExists(UUID id);
    public ApiResponse<LeaveEntity> applyLeave(LeaveEntity entity);
    public boolean isEmployeeExists(UUID id);
}
