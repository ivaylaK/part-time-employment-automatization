package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.JobApplication;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobApplicationRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JobApplicationService {

    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobRepository jobRepository, ApplicantRepository applicantRepository, JobApplicationRepository jobApplicationRepository) {
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobApplication> findAllJobApplications() {
        return StreamSupport.stream(
                        jobApplicationRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteJobApplication(Long jobId, Long applicantId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new IllegalArgumentException("Job for this application not found")
        );
        Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(
                () -> new IllegalArgumentException("Applicant for this application not found")
        );
        jobApplicationRepository.deleteByJobAndApplicant(job, applicant);
    }

    public ByteArrayResource exportApplicationsToExcel() {
        List<JobApplication> jobApplications = this.findAllJobApplications();
        try(Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("job applications");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Week");
            header.createCell(1).setCellValue("Store number");
            header.createCell(2).setCellValue("City");
            header.createCell(3).setCellValue("Client");
            header.createCell(4).setCellValue("Store");
            header.createCell(5).setCellValue("Date");
            header.createCell(6).setCellValue("Time");
            header.createCell(7).setCellValue("Name");
            header.createCell(8).setCellValue("Phone Number");
            header.createCell(9).setCellValue("Comment");
            header.createCell(10).setCellValue("Wage");

            int rowN = 1;
            for (JobApplication jobApplication : jobApplications) {
                Job job = jobApplication.getJob();
                Applicant applicant = jobApplication.getApplicant();

                Row row = sheet.createRow(rowN);

                row.createCell(0).setCellValue(job.getWeekNumbers());
                row.createCell(1).setCellValue(job.getStoreNumber());
                row.createCell(2).setCellValue(job.getCity());
                row.createCell(3).setCellValue(job.getClient());
                row.createCell(4).setCellValue(job.getLocation());
                row.createCell(5).setCellValue(job.getDatePeriod());
                row.createCell(6).setCellValue(job.getShiftPeriod());
                row.createCell(7).setCellValue(applicant.getFullName());
                row.createCell(8).setCellValue(applicant.getNumber());

                rowN++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
