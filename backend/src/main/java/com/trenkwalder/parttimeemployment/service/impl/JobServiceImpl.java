package com.trenkwalder.parttimeemployment.service.impl;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import com.trenkwalder.parttimeemployment.repository.NotificationRepository;
import com.trenkwalder.parttimeemployment.service.JobService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final NotificationRepository notificationRepository;

    public JobServiceImpl(JobRepository jobRepository, ApplicantRepository applicantRepository, NotificationRepository notificationRepository) {
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public List<Job> findAllJobs() {
        return StreamSupport.stream(
                jobRepository
                        .findAll()
                        .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Job> findJobById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public List<Applicant> getApplicantsByJobId(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found")
        );

        List<Long> applicantIds = job.getApplicants();
        return applicantIds.stream().map(
                id -> applicantRepository.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("Applicant not found")
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllClients() {
        return this.jobRepository.findDistinctClients();
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        notificationRepository.deleteAllByJob(job);
        jobRepository.deleteById(id);
    }
}
