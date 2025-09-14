package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.dto.ChosenDaysDto;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.service.JobInvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "jobs/invitation")
public class JobInvitationController {

    private final JobInvitationService jobInvitationService;

    public JobInvitationController(JobInvitationService jobInvitationService) {
        this.jobInvitationService = jobInvitationService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<String> getInvitationLink(@PathVariable Long id, @RequestParam String contact) {
        return new ResponseEntity<>(
                jobInvitationService.generateInvitation(id, contact),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/validate")
    public ResponseEntity<Boolean> validateInvitationToken(@RequestParam String token) {
        try {
            boolean isValid = jobInvitationService.validateInvitation(token);
            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/apply")
    public ResponseEntity<String> applyToJobWithToken(@RequestParam String token,
                                                      @RequestBody ChosenDaysDto chosenDaysDto,
                                                      @AuthenticationPrincipal User user) {
        try {
            jobInvitationService.applyToJob(token, chosenDaysDto.getChosenDays(), user);
            return new ResponseEntity<>("Application successful!", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/apply/{id}")
    public ResponseEntity<String> applyToJobThroughProfile(@PathVariable Long id,
                                                           @RequestBody ChosenDaysDto chosenDaysDto,
                                                           @AuthenticationPrincipal User user) {
        try {
            jobInvitationService.applyToJob(id, chosenDaysDto.getChosenDays(), user);
            return new ResponseEntity<>("Application successful!", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/admin/apply/{id}")
    public ResponseEntity<String> addApplicantAsAdmin(@PathVariable Long id,
                                                      @RequestBody ChosenDaysDto chosenDaysDto,
                                                      @RequestParam Long applicantId) {
        try {
            jobInvitationService.addApplicantAsAdmin(id, chosenDaysDto.getChosenDays(), applicantId);
            return new ResponseEntity<>("Application successful!", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
