package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.dto.JobDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.mapper.ApplicantMapper;
import com.trenkwalder.parttimeemployment.mapper.JobMapper;
import com.trenkwalder.parttimeemployment.repository.UserRepository;
import com.trenkwalder.parttimeemployment.service.ApplicantService;
import com.trenkwalder.parttimeemployment.service.JobService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final ApplicantMapper applicantMapper;
    private final JobMapper jobMapper;

    public ApplicantController(ApplicantService applicantService, JobService jobService, UserRepository userRepository, ApplicantMapper applicantMapper, JobMapper jobMapper) {
        this.applicantService = applicantService;
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.applicantMapper = applicantMapper;
        this.jobMapper = jobMapper;
    }

    @PostMapping()
    public ResponseEntity<ApplicantDto> createApplicant(@RequestBody ApplicantDto applicantDto) {
        Applicant applicant = applicantService.saveApplicant(applicantMapper.mapFrom(applicantDto));
        return new ResponseEntity<>(applicantMapper.mapTo(applicant), HttpStatus.CREATED);

    }

    @GetMapping()
    public List<ApplicantDto> getAllApplicants() {
        List<Applicant> applicants = applicantService.findAllApplicants();
        return applicants
                .stream()
                .map(applicantMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApplicantDto> getApplicantById(@PathVariable("id") Long id) {
        Optional<Applicant> foundApplicant = applicantService.findApplicantById(id);
        return foundApplicant.map(
                applicant -> new ResponseEntity<>(applicantMapper.mapTo(applicant), HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/self")
    public ResponseEntity<ApplicantDto> getUserApplicant(@AuthenticationPrincipal User user) {
        Optional<Applicant> foundApplicant = applicantService.findApplicantByUser(user);
        return foundApplicant.map(
                applicant -> new ResponseEntity<>(applicantMapper.mapTo(applicant), HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/dashboardApplied")
    public ResponseEntity<List<JobDto>> getAppliedJobs(@AuthenticationPrincipal(expression = "username") String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Job> jobs = applicantService.getAppliedJobs(user);
        List<JobDto> jobDtos = jobs.stream().map(jobMapper::mapTo).toList();

        return new ResponseEntity<>(jobDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboardAvailable")
    public ResponseEntity<List<JobDto>> getAvailableJobs(@AuthenticationPrincipal(expression = "username") String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Job> availableJobs = applicantService.getAvailableJobs(user);
        List<JobDto> jobDtos = availableJobs.stream().map(jobMapper::mapTo).toList();

        return new ResponseEntity<>(jobDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/export")
    public ResponseEntity<Resource> exportApplicantsToExcel() {
        ByteArrayResource excel = applicantService.exportApplicantsToExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applicants.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApplicantDto> updateApplicant(@RequestBody ApplicantDto applicantDto, @PathVariable("id") Long id) {
        if (applicantService.findApplicantById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Applicant updatedApplicant = applicantMapper.mapFrom(applicantDto);
        updatedApplicant.setId(id);
        applicantService.saveApplicant(updatedApplicant);
        return new ResponseEntity<>(applicantMapper.mapTo(updatedApplicant), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/rank")
    public void updateRank(@RequestBody Integer rank, @PathVariable("id") Long id) {
        applicantService.updateRank(id, rank);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteApplicant(@PathVariable("id") Long id) {
        if (applicantService.findApplicantById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        applicantService.deleteApplicant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
