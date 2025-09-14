package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.dto.JobApplicationDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.JobApplication;
import com.trenkwalder.parttimeemployment.repository.JobApplicationRepository;
import com.trenkwalder.parttimeemployment.service.ApplicantService;
import com.trenkwalder.parttimeemployment.service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper implements Mapper<JobApplication, JobApplicationDto> {

    private final JobService jobService;
    private final ApplicantService applicantService;

    public JobApplicationMapper(JobService jobService, ApplicantService applicantService) {
        this.jobService = jobService;
        this.applicantService = applicantService;
    }


    @Override
    public JobApplication mapFrom(JobApplicationDto jobApplicationDto) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(jobApplicationDto.getId());

        jobApplication.setJob(jobService.findJobById(jobApplicationDto.getJobId()).orElseThrow(
                () -> new IllegalArgumentException("Job for this application not found")
        ));
        jobApplication.setApplicant(applicantService.findApplicantById(jobApplicationDto.getApplicantId()).orElseThrow(
                () -> new IllegalArgumentException("Applicant for this application not found")
        ));
        return jobApplication;
    }

    @Override
    public JobApplicationDto mapTo(JobApplication jobApplication) {
        Job job = jobApplication.getJob();
        Applicant applicant = jobApplication.getApplicant();

        JobApplicationDto jobApplicationDto = new JobApplicationDto();
        jobApplicationDto.setId(jobApplication.getId());

        // job;
        jobApplicationDto.setJobId(job.getId());
        jobApplicationDto.setJobTitle(job.getTitle());
        jobApplicationDto.setJobClient(job.getClient());
        jobApplicationDto.setJobCity(job.getCity());
        jobApplicationDto.setJobLocation(job.getLocation());
        jobApplicationDto.setJobStoreNumber(job.getStoreNumber());
        jobApplicationDto.setJobDatePeriod(job.getDatePeriod());
        jobApplicationDto.setJobShiftPeriod(job.getShiftPeriod());

        // applicant;
        jobApplicationDto.setApplicantId(applicant.getId());
        jobApplicationDto.setApplicantFullName(applicant.getFullName());
        jobApplicationDto.setApplicantNumber(applicant.getNumber());

        return jobApplicationDto;
    }
}
