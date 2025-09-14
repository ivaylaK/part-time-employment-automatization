package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;

    // applicant
    private Long applicantId;
    private String applicantFirstName;
    private String applicantLastName;
    private String applicantNumber;
    private String applicantCity;
    private Integer applicantRank;

    // job
    private Long jobId;
    private String jobTitle;
    private String jobClient;
    private String jobCity;
    private String jobLocation;
    private String jobStoreNumber;
    private Date jobStartDate;
    private Date jobEndDate;

    private String message;

    private LocalDateTime sent;
}
