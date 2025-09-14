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
@Table(name = "job_invitations")
public class JobInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_invitations_seq_generator")
    @SequenceGenerator(name = "job_invitations_seq_generator", sequenceName = "job_invitations_id_seq", allocationSize = 1)
    private Long id;

    private String contact;
    private String token;

    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    public JobInvitation(Job job, String contact, String token, Date expiryDate) {
        this.expiryDate = expiryDate;
        this.job = job;
        this.token = token;
        this.contact = contact;
    }
}
