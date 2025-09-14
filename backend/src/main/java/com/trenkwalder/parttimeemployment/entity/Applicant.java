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

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JobApplication> applications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer rank;

    @ElementCollection
    @CollectionTable(name = "applicant_blocked_clients",
                     joinColumns = @JoinColumn(name = "applicant_id"))
    @Column(name = "client")
    private List<String> blockedClients = new ArrayList<>();

    public List<Long> getJobsApplied() {
        if (applications == null) applications = new ArrayList<>();

        List<Long> jobsApplied = new ArrayList<>();
        for(JobApplication application : applications) {
            jobsApplied.add(application.getJob().getId());
        }
        return jobsApplied;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public void addBlockedClient(String client) {
        if (client == null) return;

        String clientUpdated = client.trim();
        if (clientUpdated.isEmpty()) return;

        if (blockedClients.stream().noneMatch(b -> b.equalsIgnoreCase(clientUpdated))) blockedClients.add(clientUpdated);
    }

    public void removeBlockedClient(String client) {
        if (client == null) return;

        String clientUpdated = client.trim();

        blockedClients.removeIf(b -> b.equalsIgnoreCase(clientUpdated));
    }
}
