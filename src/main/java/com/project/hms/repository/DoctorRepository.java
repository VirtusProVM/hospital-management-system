package com.project.hms.repository;

import com.project.hms.entity.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctors, Long> {

    public Doctors findByUsername(String username);

    // findBySpecialty(String specialty);
}
