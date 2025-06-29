package com.trenkwalder.parttimeemployment.service.impl;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import com.trenkwalder.parttimeemployment.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
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
        return job.getApplicants();
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
