package com.project.hms.repository;

import com.project.hms.entity.DrugReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugReportRepository extends JpaRepository<DrugReport, Long> {
}
