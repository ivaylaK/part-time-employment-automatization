package com.trenkwalder.parttimeemployment.service.impl;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.service.ApplicantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Override
    public Applicant saveApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }

    @Override
    public List<Applicant> findAllApplicants() {
        return StreamSupport.stream(
                        applicantRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Applicant> findApplicantById(Long id) {
        return applicantRepository.findById(id);
    }

    @Override
    public void deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
    }
}
