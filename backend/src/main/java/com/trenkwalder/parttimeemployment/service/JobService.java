package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {

    Job saveJob(Job job);

    List<Job> findAllJobs();

    Optional<Job> findJobById(Long id);

    List<Applicant> getApplicantsByJobId(Long jobId);

    List<String> getAllClients();

    void deleteJob(Long id);
}
