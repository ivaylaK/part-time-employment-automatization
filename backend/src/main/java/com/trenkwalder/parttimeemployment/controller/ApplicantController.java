package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.mapper.Mapper;
import com.trenkwalder.parttimeemployment.service.ApplicantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ApplicantController {

    private final ApplicantService applicantService;
    private final Mapper<Applicant, ApplicantDto> applicantMapper;


    public ApplicantController(ApplicantService applicantService, Mapper<Applicant, ApplicantDto> applicantMapper) {
        this.applicantService = applicantService;
        this.applicantMapper = applicantMapper;
    }

    @PostMapping(path = "/applicants")
    public ResponseEntity<ApplicantDto> createApplicant(@RequestBody ApplicantDto applicantDto) {
        Applicant applicant = applicantService.saveApplicant(applicantMapper.mapFrom(applicantDto));
        return new ResponseEntity<>(applicantMapper.mapTo(applicant), HttpStatus.CREATED);

    }

    @GetMapping(path = "/applicants")
    public List<ApplicantDto> getAllApplicants() {
        List<Applicant> applicants = applicantService.findAllApplicants();
        return applicants
                .stream()
                .map(applicantMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/applicants/{id}")
    public ResponseEntity<ApplicantDto> getApplicantById(@PathVariable("id") Long id) {
        Optional<Applicant> foundApplicant = applicantService.findApplicantById(id);
        return foundApplicant.map(
                applicant -> new ResponseEntity<>(applicantMapper.mapTo(applicant), HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/applicants/{id}")
    public ResponseEntity<ApplicantDto> updateApplicant(@RequestBody ApplicantDto applicantDto, @PathVariable("id") Long id) {
        if (applicantService.findApplicantById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Applicant updatedApplicant = applicantMapper.mapFrom(applicantDto);
        updatedApplicant.setId(id);
        applicantService.saveApplicant(updatedApplicant);
        return new ResponseEntity<>(applicantMapper.mapTo(updatedApplicant), HttpStatus.OK);
    }


    @DeleteMapping(path = "/applicants/{id}")
    public ResponseEntity<Void> deleteApplicant(@PathVariable("id") Long id) {
        if (applicantService.findApplicantById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        applicantService.deleteApplicant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
