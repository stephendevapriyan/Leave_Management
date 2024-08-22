package com.example.LeaveManagementSystem.service;

import com.example.LeaveManagementSystem.dto.CreatePayslipDTO;
import com.example.LeaveManagementSystem.entity.EmployeeEntity;
import com.example.LeaveManagementSystem.entity.PayslipEntity;

import org.springframework.stereotype.Service;

import com.example.LeaveManagementSystem.repository.PayslipRep;
import com.example.LeaveManagementSystem.utils.ErrorUtil;

@Service
public class PayslipService {
    private final PayslipRep payslipRepository;
    private final LeaveService leaveService;

    public PayslipService(PayslipRep payslipRepository, LeaveService leaveService) {
        this.payslipRepository = payslipRepository;
        this.leaveService = leaveService;
    }

    public ErrorUtil<String> save(CreatePayslipDTO dto) {
        var employee = this.leaveService.isEmployeeExists(dto.employeeId());
        if (!employee) {
            return new ErrorUtil<String>(false, "employee with UUID is not found");
        }

        var emp = EmployeeEntity.builder()
                .id(dto.employeeId()).build();

        var payslip = PayslipEntity.builder()
                .file(dto.file())
                .fileName(dto.fileName())
                .fileType(dto.fileType())
                .employeeEntity(emp)
                .issuedDate(dto.issuedDate())
                .payPeriodStart(dto.payPeriodStart())
                .payPeriodEnd(dto.payPeriodEnd())
                .build();

        this.payslipRepository.save(payslip);

        return new ErrorUtil<String>(true, null);
    }
}
