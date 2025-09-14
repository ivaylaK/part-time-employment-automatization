package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Request;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobApplicationRepository;
import com.trenkwalder.parttimeemployment.repository.RequestRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RequestService {

    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final RequestRepository requestRepository;
    private final NotificationService notificationService;

    public RequestService(JobRepository jobRepository, ApplicantRepository applicantRepository, JobApplicationRepository jobApplicationRepository, RequestRepository requestRepository, NotificationService notificationService) {
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.requestRepository = requestRepository;
        this.notificationService = notificationService;
    }

    public Request create(Long jobId, Long applicantId, String message, String type) {
        if (!Objects.equals(type, Request.TYPE_APPLY)
                && !Objects.equals(type, Request.TYPE_UNAPPLY)) {
            throw new IllegalArgumentException("Unsupported request type: " + type);
        }

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new IllegalArgumentException("Job not found")
        );
        Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(
                () -> new IllegalArgumentException("Applicant not found")
        );

        Request request = new Request();
        request.setJob(job);
        request.setApplicant(applicant);
        request.setMessage(message);
        request.setType(type);
        request.setSent(java.time.LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Transactional
    public List<Request> findAll() {
        return StreamSupport.stream(
                        requestRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveAndDelete(Long id) {
        Request request
                = requestRepository.findById(id).orElseThrow();

        Applicant applicant = request.getApplicant();

        String message = "";

        if (Request.TYPE_APPLY.equals(request.getType())) {
            applicant.setRank(1);
            applicantRepository.save(applicant);
            message = "Your request has been approved. You can now apply to jobs.";

        } else if (Request.TYPE_UNAPPLY.equals(request.getType())) {
            Job job = request.getJob();
            jobApplicationRepository.deleteByJobAndApplicant(job, applicant);
            message = "Your request has been approved. You have been successfully unregistered from the following job: "
                    + job.getCity() + ", " + job.getLocation() + ", " + job.getClient() + ", " + job.getDatePeriod();
        }

        User user = applicant.getUser();
        if (user != null) {
            notificationService.create(user, message);
        }

        requestRepository.delete(request);
    }

    @Transactional
    public void delete(Long id) {
        Request request
                = requestRepository.findById(id).orElseThrow();

        requestRepository.delete(request);
    }
}
