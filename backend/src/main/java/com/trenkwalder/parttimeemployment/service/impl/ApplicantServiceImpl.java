package com.trenkwalder.parttimeemployment.service.impl;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import com.trenkwalder.parttimeemployment.service.ApplicantService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository, JobRepository jobRepository) {
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
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
    public Optional<Applicant> findApplicantByUser(User user) {
        return applicantRepository.findByUser(user);
    }

    @Override
    public List<Job> getAppliedJobs(User user) {
        Applicant applicant = applicantRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found"));
        return applicant.getJobsApplied();
    }

    @Override
    public List<Job> getAvailableJobs(User user) {
        Applicant applicant = applicantRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found"));

        List<Job> appliedJobs = applicant.getJobsApplied();
        List<Job> allJobsAvailable = jobRepository.findJobsWithAvailableSlots();

        return allJobsAvailable.stream()
                .filter(job -> !appliedJobs.contains(job))
                .toList();
    }

    @Override
    public ByteArrayResource exportApplicantsToExcel() {
        List<Applicant> applicants = this.findAllApplicants();
        try(Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("applicants");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("First Name");
            header.createCell(1).setCellValue("Last Name");
            header.createCell(2).setCellValue("Number");
            header.createCell(3).setCellValue("City");
            header.createCell(4).setCellValue("Rank");

            int rowN = 1;
            for (Applicant applicant : applicants) {
                Row row = sheet.createRow(rowN);
                row.createCell(0).setCellValue(applicant.getFirstName());
                row.createCell(1).setCellValue(applicant.getLastName());
                row.createCell(2).setCellValue(applicant.getNumber());
                row.createCell(3).setCellValue(applicant.getCity());
                row.createCell(4).setCellValue(applicant.getRank() != null ? applicant.getRank() : 0);
                rowN++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRank(Long id, Integer rank) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found"));
        applicant.setRank(rank);
        applicantRepository.save(applicant);
    }

    @Override
    public void deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
    }
}
