package com.trenkwalder.parttimeemployment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private String client;
    private String city;
    private String location;
    private String storeNumber;

    private Date startDate;
    private Date endDate;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    private String personInCharge;
    private String personInChargeNumber;

    private Integer totalSlots;
    private Integer applicantsCount;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobInvitation> jobInvitations;

    public List<JobApplication> getApplications() {
        if (applications == null) applications = new ArrayList<>();
        return applications;
    }

    public List<Long> getApplicants() {
        if (applications == null) applications = new ArrayList<>();

        List<Long> applicants = new ArrayList<>();
        for(JobApplication application : applications) {
            applicants.add(application.getApplicant().getId());
        }
        return applicants;
    }

    public int getApplicantsCount() {
        if (applications == null) return 0;
        return applications.size();
    }

    public int getRemainingSlots() {
        return totalSlots - getApplicantsCount();
    }

    public boolean hasAvailableSlots() {
        return getApplicantsCount() < totalSlots;
    }

    public int getStartWeekNumber() {
        return startDate.toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public int getEndWeekNumber() {
        return endDate.toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public String getDatePeriod() {
        return this.startDate + " - " + this.endDate;
    }

    public String getShiftPeriod() {
        return this.shiftStart + " - " + this.shiftEnd;
    }

    public String getWeekNumbers() {
        return this.getStartWeekNumber() + " - " + this.getEndWeekNumber();
    }
}
