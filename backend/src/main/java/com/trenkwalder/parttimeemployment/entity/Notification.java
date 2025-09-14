package com.trenkwalder.parttimeemployment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_seq_generator")
    @SequenceGenerator(name = "notifications_seq_generator", sequenceName = "notifications_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    private boolean read = false;

    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne(optional = true)
    @JoinColumn(name = "job_id")
    private Job job;
}
