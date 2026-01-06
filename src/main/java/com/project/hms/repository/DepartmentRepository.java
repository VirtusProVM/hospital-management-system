package com.project.hms.repository;

import com.project.hms.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {
}
