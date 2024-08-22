package com.example.LeaveManagementSystem.controller;

import com.example.LeaveManagementSystem.entity.EmployeeEntity;
import com.example.LeaveManagementSystem.entity.LeaveEntity;
import com.example.LeaveManagementSystem.entity.OrganizationEntity;
import com.example.LeaveManagementSystem.response.ApiResponse;
import com.example.LeaveManagementSystem.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LeaveController {

    @Autowired
    private LeaveService service;

    @PostMapping("/organization")
    public ResponseEntity<ApiResponse<OrganizationEntity>> addOrganization(@RequestBody OrganizationEntity entity){
        ApiResponse<OrganizationEntity> response=service.saveOrganization(entity);
        return new ResponseEntity<>(response,  HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/employee")
    public ResponseEntity<ApiResponse<EmployeeEntity>> addEmployee(@RequestBody EmployeeEntity entity){
       ApiResponse<EmployeeEntity> response= service.saveEmployee(entity);
        return new ResponseEntity<>(response,  HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/leave")
   public ResponseEntity<ApiResponse<LeaveEntity>> leaveApply(@RequestBody LeaveEntity entity){
      ApiResponse<LeaveEntity>response= service.applyLeave(entity);
        return new ResponseEntity<>(response,  HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/deleteorg" )
    ResponseEntity<ApiResponse<OrganizationEntity>> deleteOLM(@RequestParam(name="id",required = false) UUID id){

        return service.deleteOrganizationID(id);
    }


    @DeleteMapping("/deleteemployee")
    ResponseEntity<ApiResponse<EmployeeEntity>> employeedelete(@RequestParam (name="id",required = false)UUID id) {
        return service.deleteEmployeeById(id);
    }

    @DeleteMapping("/deleteleave")
    ResponseEntity<ApiResponse<LeaveEntity>> deleteLeave(@RequestParam (name="id",required = false)UUID id){

        return service.deleteLeave(id);
    }


}
