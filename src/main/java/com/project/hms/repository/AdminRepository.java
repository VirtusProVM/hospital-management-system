package com.project.hms.repository;

import com.project.hms.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Admin findByUsername(String username);
}
