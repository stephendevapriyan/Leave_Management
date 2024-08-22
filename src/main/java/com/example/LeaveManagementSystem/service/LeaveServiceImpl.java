package com.example.LeaveManagementSystem.service;

import com.example.LeaveManagementSystem.entity.EmployeeEntity;
import com.example.LeaveManagementSystem.entity.LeaveEntity;
import com.example.LeaveManagementSystem.entity.OrganizationEntity;
import com.example.LeaveManagementSystem.repository.EmployeeRepo;
import com.example.LeaveManagementSystem.repository.LeaveRepo;
import com.example.LeaveManagementSystem.repository.OrganizationRepo;
import com.example.LeaveManagementSystem.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private OrganizationRepo orepo;
    @Autowired
    private EmployeeRepo erepository;
    @Autowired
    private LeaveRepo leaverepo;

    // save organization
    @Override
    public ApiResponse<OrganizationEntity> saveOrganization(OrganizationEntity oentity) {
        log.info("save organization method started");
        try {
            if (organizationEmailExists(oentity.getEmail()) && checkLocation(oentity.getLocation())) {
                log.warn("already registered company with give email and location");
                return ApiResponse.<OrganizationEntity>builder()
                        .message("Company is already registered for the given email and location")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            OrganizationEntity savedEntity = orepo.save(oentity);
            log.info("successfully saved organization");
            return ApiResponse.<OrganizationEntity>builder()
                    .message("successfully saved organization")
                    .status(HttpStatus.OK.value())
                    .data(savedEntity)
                    .build();
        } catch (Exception e) {
            log.error("invalid input please check");
            return ApiResponse.<OrganizationEntity>builder()
                    .message("invalid input please check")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .build();
        } finally {
            log.info("save organization method completed");
        }
    }

    @Override
    public boolean organizationEmailExists(String email) {
        log.info("checking organization email method started");
        if (orepo.findByEmail(email).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLocation(String location) {
        log.info("checking organization location method started");
        if (orepo.findByLocation(location).isPresent()) {
            return true;
        }
        return false;
    }

    // save employee
    @Override
    public ApiResponse<EmployeeEntity> saveEmployee(EmployeeEntity entity) {
        log.info("saving employee method started");
        try {
            if (isEmailExists(entity.getEmail())) {
                log.warn("Email id already exists");
                return ApiResponse.<EmployeeEntity>builder()
                        .message("Email id already exists")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            if (entity.getOrganization() == null || !isOrganizationExists(entity.getOrganization().getId())) {
                log.warn("Invalid organization");
                return ApiResponse.<EmployeeEntity>builder()
                        .message("Invalid organization")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            EmployeeEntity savedEntity = erepository.save(entity);
            log.info("Successfully saved employee");
            return ApiResponse.<EmployeeEntity>builder()
                    .status(HttpStatus.OK.value())
                    .message("Successfully saved")
                    .data(savedEntity)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("invalid input please check");
            return ApiResponse.<EmployeeEntity>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("invalid input please check")
                    .data(null)
                    .build();
        } finally {
            log.info("save employee method completed");
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        log.info(" employee email exists method started");
        if (erepository.findByEmail(email).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isOrganizationExists(UUID id) {
        log.info(" organization exists method started");
        if (orepo.findById(id).isPresent()) {
            return true;
        }
        return false;
    }

    // apply leave

    @Override
    public ApiResponse<LeaveEntity> applyLeave(LeaveEntity entity) {
        log.info("apply leave method started");
        try {
            if (!isEmployeeExists(entity.getEmployee().getId())) {
                log.warn("invalid employee id");
                return ApiResponse.<LeaveEntity>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("invalid employee id")
                        .data(null)
                        .build();
            }
            LeaveEntity saved = leaverepo.save(entity);
            log.info("succesfully applied leave");
            return ApiResponse.<LeaveEntity>builder()
                    .status(HttpStatus.OK.value())
                    .message("succesfully applied leave")
                    .data(saved)
                    .build();
        } catch (Exception e) {
            log.error("invalid input please check");
            return ApiResponse.<LeaveEntity>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("invalid input please check")
                    .data(null)
                    .build();
        } finally {
            log.info("apply leave method completed");
        }
    }

    @Override
    public boolean isEmployeeExists(UUID id) {
        log.info("employee exists method started");
        if (erepository.findById(id).isPresent()) {
            return true;
        }
        return false;
    }
}
