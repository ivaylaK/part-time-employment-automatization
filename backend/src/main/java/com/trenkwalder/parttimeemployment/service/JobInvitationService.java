package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.entity.*;
import com.trenkwalder.parttimeemployment.mapper.ApplicantMapper;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobApplicationRepository;
import com.trenkwalder.parttimeemployment.repository.JobInvitationRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobInvitationService {

    private final JobInvitationRepository jobInvitationRepository;
    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final MessageService messageService;
    private final ApplicantMapper applicantMapper;

    public JobInvitationService(JobInvitationRepository jobInvitationRepository, JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, MessageService messageService, ApplicantRepository applicantRepository, ApplicantMapper applicantMapper) {
        this.jobInvitationRepository = jobInvitationRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.messageService = messageService;
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
    }

    public String generateInvitation(Long jobId, String contact) {
        String token = UUID.randomUUID().toString();

        Job job = jobRepository.findById(jobId).orElseThrow(()
                        -> new IllegalArgumentException("Job not found! JobId " + jobId + " is invalid!")
                );
        Date expiryDate = job.getEndDate();

        JobInvitation invitation = new JobInvitation(job, contact, token, expiryDate);
        jobInvitationRepository.save(invitation);

        String invitationLink = "http://localhost:4200/jobs/apply?token=" + token;

        String invitationMessage = "Here is your invitation to apply for a job: " + invitationLink;
//        messageService.sendSmsToOneApplicant(invitationMessage, contact);

        return invitationLink;
    }

    public boolean validateInvitation(String token) {
        JobInvitation jobInvitation = jobInvitationRepository.findByToken(token).orElseThrow(()
                -> new IllegalArgumentException("Token " + token + " is invalid!")
            );
        if (jobInvitation.getExpiryDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Token " + token + " expired!");
        }
        return true;
    }

    @Transactional
    public void applyToJob(String token, List<Date> chosenDays, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to apply.");
        }

        JobInvitation invitation = jobInvitationRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (invitation.getExpiryDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        Job job = jobRepository.findById(invitation.getJob().getId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Applicant applicant = this.ensureUserIsConnectedToApplicant(user);

        if (applicant.getRank() == null) {
            throw new IllegalStateException("You're not allowed to apply yet");
        }

        applyForSpecificDays(applicant, job, chosenDays);
    }

    @Transactional
    public void applyToJob(Long jobId, List<Date> chosenDays, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to apply.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Applicant applicant = this.ensureUserIsConnectedToApplicant(user);

        if (applicant.getRank() == null) {
            throw new IllegalStateException("You're not allowed to apply yet");
        }

        applyForSpecificDays(applicant, job, chosenDays);
    }

    @Transactional
    public void addApplicantAsAdmin(Long jobId, List<Date> chosenDays, Long applicantId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalStateException("Applicant not found"));

        if (applicant.getUser() == null) {
            throw new IllegalArgumentException("Applicant must be connected to a user account");
        }

        applyForSpecificDays(applicant, job, chosenDays);
    }

    private void applyForSpecificDays(Applicant applicant, Job job, List<Date> days) {
        if (job.getApplicantsCount() >= job.getTotalSlots()) {
            throw new IllegalStateException("Job has no available slots");
        }

        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("Please select at least one day.");
        }

        if (jobApplicationRepository.existsByApplicantAndJob(applicant, job)) {
            throw new IllegalStateException("You already applied for this job.");
        }

        Date start = job.getStartDate();
        Date end   = job.getEndDate();
        for (Date date : days) {
            if (date.before(start) || date.after(end)) {
                throw new IllegalArgumentException("Selected day " + date + " is outside the job period.");
            }
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setApplicant(applicant);

        List<ApplicationDay> applicationDays = new ArrayList<>();
        for (Date date : days.stream().distinct().sorted().toList()) {
            ApplicationDay applicationDay = new ApplicationDay();
            applicationDay.setApplication(jobApplication);
            applicationDay.setChosenDay(date);
            applicationDays.add(applicationDay);
        }
        jobApplication.setApplicationDays(applicationDays);

        jobApplicationRepository.save(jobApplication);
    }


    private Applicant ensureUserIsConnectedToApplicant(User user) {
        if (user.getApplicant() == null) {
            throw new IllegalArgumentException("Applicant must be connected to a user account");
        }

        return applicantRepository.findById(user.getApplicant().getId())
                .orElseThrow(() -> new IllegalStateException("Applicant not found"));
    }
}
