package com.armi.infrastructure.adapter.in.web;

import com.armi.model.IncidentReport;
import com.armi.repository.IncidentReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentWebAdapter {

    private final IncidentReportRepository incidentReportRepository;

    public IncidentWebAdapter(IncidentReportRepository incidentReportRepository) {
        this.incidentReportRepository = incidentReportRepository;
    }

    @GetMapping
    public ResponseEntity<List<IncidentReport>> getAllIncidents() {
        return ResponseEntity.ok(incidentReportRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<IncidentReport> createIncident(@RequestBody IncidentReport incident) {
        return ResponseEntity.ok(incidentReportRepository.save(incident));
    }
}
