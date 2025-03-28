package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {

    Applicant saveApplicant(Applicant applicant);

    List<Applicant> findAllApplicants();

    Optional<Applicant> findApplicantById(Long id);

    void deleteApplicant(Long id);
}
