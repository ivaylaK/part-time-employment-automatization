package com.trenkwalder.parttimeemployment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_applications_seq_generator")
    @SequenceGenerator(name = "job_applications_seq_generator", sequenceName = "job_applications_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnore
    private Job job;

    @ManyToOne
    @JoinColumn(name = "applicants_id", nullable = false)
    @JsonIgnore
    private Applicant applicant;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationDay> applicationDays = new ArrayList<>();
}
