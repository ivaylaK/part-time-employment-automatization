package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Long id;

    private String message;

    private boolean read;

    private LocalDateTime created;

    // job
    private Long jobId;
    private String jobTitle;
    private String jobClient;
    private String jobCity;
    private String jobLocation;
    private Date jobStartDate;
    private Date jobEndDate;
}
