package com.example.LeaveManagementSystem.service;

import com.example.LeaveManagementSystem.dto.EmployeeResponseDTO;
import com.example.LeaveManagementSystem.dto.LeaveResponseDTO;
import com.example.LeaveManagementSystem.entity.AcceptLeaveEntity;
import com.example.LeaveManagementSystem.entity.EmployeeEntity;
import com.example.LeaveManagementSystem.entity.LeaveEntity;
import com.example.LeaveManagementSystem.entity.LeaveStatus;
import com.example.LeaveManagementSystem.entity.OrganizationEntity;
import com.example.LeaveManagementSystem.entity.RejectLeaveEntity;
import com.example.LeaveManagementSystem.exceptions.UserNotFoundException;
import com.example.LeaveManagementSystem.repository.AcceptLeaveEntityRepo;
import com.example.LeaveManagementSystem.repository.EmployeeRepo;
import com.example.LeaveManagementSystem.repository.LeaveRepo;
import com.example.LeaveManagementSystem.repository.OrganizationRepo;
import com.example.LeaveManagementSystem.repository.RejectLeaveEntityRepo;
import com.example.LeaveManagementSystem.response.ApiResponse;
import com.example.LeaveManagementSystem.utils.ErrorUtil;

import com.example.LeaveManagementSystem.validation.EmailValidation;
import com.example.LeaveManagementSystem.validation.MobileNoValidation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
    @Autowired
    private AcceptLeaveEntityRepo acceptLeaveEntityRepo;
    @Autowired
    private RejectLeaveEntityRepo rejectLeaveEntityRepo;
    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private MobileNoValidation mobileNoValidation;

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
            if(!emailValidation.isEmailValid(oentity.getEmail())){
                log.warn("invalid email id ");
                return ApiResponse.<OrganizationEntity>builder()
                        .message("invalid email id please check")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            if(!mobileNoValidation.isNumberValid(oentity.getContactNumber())){
                log.warn("invalid mobile no");
                return ApiResponse.<OrganizationEntity>builder()
                        .message("invalid mobile no please check")
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
            log.error("invalid input please check" +e.getMessage());
            return ApiResponse.<OrganizationEntity>builder()
                    .message("invalid input please check" +e.getMessage())
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
    public ApiResponse<EmployeeResponseDTO> saveEmployee(EmployeeEntity entity) {
        log.info("saving employee method started");
        try {
            if (isEmailExists(entity.getEmail())) {
                log.warn("Email id already exists");
                return ApiResponse.<EmployeeResponseDTO>builder()
                        .message("Email id already exists")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            if (entity.getOrganization() == null || !isOrganizationExists(entity.getOrganization().getId())) {
                log.warn("Invalid organization");
                return ApiResponse.<EmployeeResponseDTO>builder()
                        .message("Invalid organization")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            if(!emailValidation.isEmailValid(entity.getEmail())){
                log.warn("invalid email id ");
                return ApiResponse.<EmployeeResponseDTO>builder()
                        .message("invalid email id please check")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            if(!mobileNoValidation.isNumberValid(entity.getPhoneNumber())){
                log.warn("invalid mobile no ");
                return ApiResponse.<EmployeeResponseDTO>builder()
                        .message("invalid mobile no")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build();
            }
            EmployeeEntity savedEntity = erepository.save(entity);
            log.info("Successfully saved employee");

            EmployeeResponseDTO dto = new EmployeeResponseDTO();

            dto.setId(entity.getId());
            dto.setFirstname(entity.getFirstname());
            dto.setLastname(entity.getLastname());
            dto.setEmail(entity.getEmail());
            dto.setRole(entity.getRole());
            dto.setPhoneNumber(entity.getPhoneNumber());
            dto.setHireDate(entity.getHireDate());
            dto.setJobTitle(entity.getJobTitle());
            dto.setOrganizationId(entity.getOrganization().getId());
            dto.setActive(entity.isActive());
            dto.setCreatedAt(entity.getCreatedAt());
            dto.setUpdatedAt(entity.getUpdatedAt());

            return ApiResponse.<EmployeeResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Successfully saved")
                    .data(dto)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("invalid input please check");
            return ApiResponse.<EmployeeResponseDTO>builder()
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
    public ApiResponse<LeaveResponseDTO> applyLeave(LeaveEntity entity) {
        log.info("apply leave method started");
        try {
            if (!isEmployeeExists(entity.getEmployee().getId())) {
                log.warn("invalid employee id");
                return ApiResponse.<LeaveResponseDTO>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("invalid employee id")
                        .data(null)
                        .build();
            }
            LeaveEntity saved = leaverepo.save(entity);
            log.info("succesfully applied leave");

            LeaveResponseDTO dto = new LeaveResponseDTO();
            dto.setId(entity.getId());
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setStartDate(entity.getStartDate());
            dto.setEndDate(entity.getEndDate());
            dto.setLeaveType(entity.getLeaveType());
            dto.setStatus(entity.getStatus());
            dto.setRequestDate(entity.getRequestDate());
            dto.setLeaveReason(entity.getLeaveReason());
            dto.setApprovedDate(entity.getApprovedDate());
            dto.setCreatedAt(entity.getCreatedAt());
            dto.setUpdatedAt(entity.getUpdatedAt());
            return ApiResponse.<LeaveResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("succesfully applied leave")
                    .data(dto)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("invalid input please check");
            return ApiResponse.<LeaveResponseDTO>builder()
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

    // stephenDevepriyan

    public ResponseEntity<ApiResponse<OrganizationEntity>> deleteOrganizationID(UUID id) {
        log.info("Attempting to delete organization with ID: {}", id);

        try {
            // Fetch the organization entity by ID
            Optional<OrganizationEntity> optionalOrganization = orepo.findById(id);

            if (optionalOrganization.isEmpty()) {
                String errorMessage = "The organization with ID " + id + " was not found.";
                log.error(errorMessage);
                ApiResponse<OrganizationEntity> response = ApiResponse.<OrganizationEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            OrganizationEntity organizationEntity = optionalOrganization.get();

            // Check if the organization is already marked as deleted
            if (organizationEntity.isDelete()) {
                String errorMessage = "The organization with ID " + id + " is already marked as deleted.";
                log.warn(errorMessage);
                ApiResponse<OrganizationEntity> response = ApiResponse.<OrganizationEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.CONFLICT.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Mark the organization entity as deleted and update the deletion timestamp
            organizationEntity.setDelete(true);
            organizationEntity.setDeletedAt(LocalDateTime.now());

            // Save the changes to the repository
            orepo.save(organizationEntity);

            ApiResponse<OrganizationEntity> response = ApiResponse.<OrganizationEntity>builder()
                    .message("Organization successfully marked as deleted")
                    .status(HttpStatus.OK.value())
                    .data(organizationEntity)
                    .build();

            log.info("Successfully marked organization with ID {} as deleted", id);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException ex) {
            log.error("RuntimeException occurred while deleting organization with ID {}: {}", id, ex.getMessage(), ex);
            ApiResponse<OrganizationEntity> response = ApiResponse.<OrganizationEntity>builder()
                    .message("An error occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("Exception occurred while deleting organization with ID {}: {}", id, ex.getMessage(), ex);
            ApiResponse<OrganizationEntity> response = ApiResponse.<OrganizationEntity>builder()
                    .message("An unexpected error occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<EmployeeEntity>> deleteEmployeeById(UUID id) {
        log.info("Attempting to delete employee with ID: {}", id);

        try {
            // Fetch the employee entity by ID
            Optional<EmployeeEntity> optionalEmployee = erepository.findById(id);

            // Check if the employee entity is present
            if (optionalEmployee.isEmpty()) {
                String errorMessage = "The employee with ID " + id + " was not found.";
                log.error(errorMessage);
                ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            EmployeeEntity employeeEntity = optionalEmployee.get();

            // Check if the employee is already marked as deleted
            if (employeeEntity.isDelete()) {
                String errorMessage = "The employee with ID " + id + " is already marked as deleted.";
                log.warn(errorMessage);
                ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.CONFLICT.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Mark the employee entity as deleted and update the deletion timestamp
            employeeEntity.setDelete(true);
            employeeEntity.setDeletedAt(LocalDateTime.now());

            // Save the changes to the repository
            erepository.save(employeeEntity);

            ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()

                    .message("Employee successfully marked as deleted")
                    .status(HttpStatus.OK.value())
                    .data(employeeEntity)
                    .build();

            log.info("Successfully marked employee with ID {} as deleted", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NullPointerException e) {
            log.error("NullPointerException occurred while deleting employee with ID {}: {}", id, e.getMessage(), e);
            ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()
                    .message("A null pointer exception occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error("RuntimeException occurred while deleting employee with ID {}: {}", id, e.getMessage(), e);
            ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()

                    .message("An error occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Exception occurred while deleting employee with ID {}: {}", id, e.getMessage(), e);
            ApiResponse<EmployeeEntity> response = ApiResponse.<EmployeeEntity>builder()
                    .message("An unexpected error occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<LeaveEntity>> deleteLeave(UUID id) {
        log.info("Attempting to delete leave with ID: {}", id);

        try {
            // Fetch the leave entity by ID
            Optional<LeaveEntity> optionalLeave = leaverepo.findById(id);

            if (optionalLeave.isEmpty()) {
                String errorMessage = "The leave with ID " + id + " was not found.";
                log.error(errorMessage);
                ApiResponse<LeaveEntity> response = ApiResponse.<LeaveEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            LeaveEntity leaveEntity = optionalLeave.get();

            // Check if the leave is already marked as deleted
            if (leaveEntity.isDelete()) {
                String errorMessage = "The leave with ID " + id + " is already marked as deleted.";
                log.warn(errorMessage);
                ApiResponse<LeaveEntity> response = ApiResponse.<LeaveEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.CONFLICT.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Check if the leave status is "approved" or "rejected"
            String status = leaveEntity.getStatus();
            if ("approved".equalsIgnoreCase(status) || "rejected".equalsIgnoreCase(status)) {
                String errorMessage = "Cannot delete the leave with ID " + id + " as it is " + status + ".";
                log.warn(errorMessage);
                ApiResponse<LeaveEntity> response = ApiResponse.<LeaveEntity>builder()
                        .message(errorMessage)
                        .status(HttpStatus.CONFLICT.value())
                        .data(null)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Mark the leave entity as deleted and update the deletion timestamp
            leaveEntity.setDelete(true);
            leaveEntity.setDeletedAt(LocalDateTime.now());

            // Save the changes to the repository
            leaverepo.save(leaveEntity);

            ApiResponse<LeaveEntity> response = ApiResponse.<LeaveEntity>builder()
                    .message("Leave successfully marked as deleted")
                    .status(HttpStatus.OK.value())
                    .data(leaveEntity)
                    .build();

            log.info("Successfully marked leave with ID {} as deleted", id);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception occurred while deleting leave with ID {}: {}", id, e.getMessage(), e);
            ApiResponse<LeaveEntity> response = ApiResponse.<LeaveEntity>builder()
                    .message("An unexpected error occurred while processing the request.")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SneakyThrows
    public boolean hasEnoughLeaves(UUID id, int requiredDays) {
        Optional<EmployeeEntity> employee = erepository.findById(id);
        if (employee.isEmpty())
            throw new UserNotFoundException();

        if (employee.get().getLeaveCount() < requiredDays) {
            return false;
        }
        return true;
    }

    public ErrorUtil<String> acceptLeave(AcceptLeaveEntity entity) {
        if (!isEmployeeExists(entity.getReviewedBy().getId())) {
            return new ErrorUtil<>(false, "Not a valid reviewer");
        }

        var leave = leaverepo.findById(entity.getLeaveRequest().getId());
        if (leave.isEmpty()) {
            return new ErrorUtil<>(false, "Not a valid leave ID");
        }

        int requiredDays = (int) leave.get().getStartDate().until(
                leave.get().getEndDate(),
                ChronoUnit.DAYS);

        if (!hasEnoughLeaves(leave.get().getEmployee().getId(), requiredDays)) {
            return new ErrorUtil<>(false, "Employees does not have enough leave");
        }

        if (rejectLeaveEntityRepo.findById(entity.getLeaveRequest().getId()).isPresent()) {
            entity.setStatus(LeaveStatus.APPROVED);
        } else {
            entity.setStatus(LeaveStatus.REAPPROVED);
        }
        acceptLeaveEntityRepo.save(entity);

        EmployeeEntity employee = leave.get().getEmployee();
        employee.setLeaveCount(employee.getLeaveCount() - requiredDays);
        erepository.save(employee);

        return new ErrorUtil<>(true, "leave accepted succesfully");
    }

    @Override
    public ErrorUtil<String> rejectLeave(RejectLeaveEntity entity) {
        if (!isEmployeeExists(entity.getReviewedBy().getId())) {
            return new ErrorUtil<>(false, "Not a valid reviewer");
        }

        var leave = leaverepo.findById(entity.getLeaveRequest().getId());
        if (leave.isEmpty()) {
            return new ErrorUtil<>(false, "Not a valid leave ID");
        }

        entity.setStatus(LeaveStatus.REJECTED);
        rejectLeaveEntityRepo.save(entity);

        return new ErrorUtil<String>(true, "leave rejected successfully");
    }
}
