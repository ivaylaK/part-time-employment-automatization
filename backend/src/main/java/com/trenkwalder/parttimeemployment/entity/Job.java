package com.trenkwalder.parttimeemployment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private String description;
    private String company;
    private String city;
    private String location;

    private Date startDate;
    private Date endDate;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    private Integer totalSlots;
    private Integer applicantsCount;

    @ManyToMany
    @JoinTable(name = "jobs_applicants",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "applicants_id"))
    @JsonIgnore
    private List<Applicant> applicants;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobInvitation> jobInvitations;

    public List<Applicant> getApplicants() {
        if (applicants == null) applicants = new ArrayList<>();
        return applicants;
    }

    public int getRemainingSlots() {
        return totalSlots - applicantsCount;
    }

    public boolean hasAvailableSlots() {
        return applicantsCount < totalSlots;
    }
}
