package com.trenkwalder.parttimeemployment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobs_seq_generator")
    @SequenceGenerator(name = "jobs_seq_generator", sequenceName = "jobs_id_seq", allocationSize = 1)

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
