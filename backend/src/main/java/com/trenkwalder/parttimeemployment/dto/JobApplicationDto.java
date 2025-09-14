package com.trenkwalder.parttimeemployment.dto;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDto {

    private Long id;

//    private Job job;
    private Long jobId;
    private String jobTitle;
    private String jobClient;
    private String jobCity;
    private String jobLocation;
    private String jobStoreNumber;
    private String jobDatePeriod;
    private String jobShiftPeriod;

//    private Applicant applicant;
    private Long applicantId;
    private String applicantFullName;
    private String applicantNumber;
}
