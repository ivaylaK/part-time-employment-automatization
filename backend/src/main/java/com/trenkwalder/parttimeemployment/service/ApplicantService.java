package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.User;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {

    Applicant saveApplicant(Applicant applicant);

    List<Applicant> findAllApplicants();

    Optional<Applicant> findApplicantById(Long id);

    Optional<Applicant> findApplicantByUser(User user);

    List<Job> getAppliedJobs(User user);

    List<Job> getAvailableJobs(User user);

    ByteArrayResource exportApplicantsToExcel();

    void updateRank(Long id, Integer rank);

    void deleteApplicant(Long id);
}
