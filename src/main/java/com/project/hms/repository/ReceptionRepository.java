package com.project.hms.repository;

import com.project.hms.entity.Reception;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {
    Reception findByUsername(String username);
}
