package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {

    private Long id;

    private String title;
    private String company;
    private String city;
    private String location;

    private Date startDate;
    private Date endDate;
    private Time shiftStart;
    private Time shiftEnd;

    private double hourlyPay;

    private int availableSlots;
}
