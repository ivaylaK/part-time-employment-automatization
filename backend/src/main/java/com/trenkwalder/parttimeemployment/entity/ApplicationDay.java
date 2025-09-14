package com.trenkwalder.parttimeemployment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_days")

public class ApplicationDay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_days_seq")
    @SequenceGenerator(name = "application_days_seq", sequenceName = "application_days_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication application;

    @Column(nullable = false)
    private Date chosenDay;
}
