package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.JobApplicationDto;
import com.trenkwalder.parttimeemployment.entity.JobApplication;
import com.trenkwalder.parttimeemployment.mapper.JobApplicationMapper;
import com.trenkwalder.parttimeemployment.service.JobApplicationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "jobs/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplicationController(JobApplicationService jobApplicationService, JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @GetMapping()
    public List<JobApplicationDto> getAllJobApplications() {
        List<JobApplication> jobApplications = jobApplicationService.findAllJobApplications();
        return jobApplications
                .stream()
                .map(jobApplicationMapper::mapTo)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete/{jobId}/{applicantId}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long jobId, @PathVariable Long applicantId) {
        jobApplicationService.deleteJobApplication(jobId, applicantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/export")
    public ResponseEntity<Resource> exportApplicantsToExcel() {
        ByteArrayResource excel = jobApplicationService.exportApplicationsToExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applicants.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }
}
