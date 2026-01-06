package com.project.hms.repository;

import com.project.hms.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    public Cashier findByUsername(String username);
}
