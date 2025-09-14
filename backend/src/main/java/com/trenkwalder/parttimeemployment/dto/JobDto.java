package com.trenkwalder.parttimeemployment.dto;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {

    private Long id;

    private String title;
    private String description;
    private String client;
    private String city;
    private String location;
    private String storeNumber;

    private Date startDate;
    private Date endDate;
    private String shiftStart;
    private String shiftEnd;

    private String personInCharge;
    private String personInChargeNumber;

    private Integer totalSlots;
    private Integer applicantsCount;
}
