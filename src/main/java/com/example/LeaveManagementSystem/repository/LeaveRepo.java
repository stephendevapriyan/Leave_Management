package com.example.LeaveManagementSystem.repository;

import com.example.LeaveManagementSystem.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveEntity, UUID> {
}
