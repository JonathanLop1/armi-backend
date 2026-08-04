package com.armi.repository;

import com.armi.model.AppUser;
import com.armi.model.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByDriver(AppUser driver);
}
