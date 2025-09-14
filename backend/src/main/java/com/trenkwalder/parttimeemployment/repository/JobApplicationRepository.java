package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.JobApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends CrudRepository<JobApplication, Long> {

    List<JobApplication> findAllByJob(Job job);

    List<JobApplication> findAllByApplicant(Applicant applicant);

    void deleteByJobAndApplicant(Job job, Applicant applicant);

    boolean existsByApplicantAndJob(Applicant applicant, Job job);
}
