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
@Table(name = "applicants")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicants_seq_generator")
    @SequenceGenerator(name = "applicants_seq_generator", sequenceName = "applicants_id_seq", allocationSize = 1)
    private Long id;

    private String firstName;
    private String lastName;

    private String number;
    private String city;

    @ManyToMany(mappedBy = "applicants")
    @JsonIgnore
    private List<Job> jobsApplied;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public List<Job> getJobsApplied() {
        if (jobsApplied == null) jobsApplied = new ArrayList<>();
        return jobsApplied;
    }
    private Integer rank;
}
