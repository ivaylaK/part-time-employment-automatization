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
@Table(name = "requests")
public class Request {

    public static final String TYPE_APPLY = "APPLY";
    public static final String TYPE_UNAPPLY = "UNAPPLY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Applicant applicant;

    @ManyToOne(optional = true)
    private Job job;

    private String message;

    @Column(nullable = false)
    private String type = TYPE_APPLY;

    private LocalDateTime sent = LocalDateTime.now();
}
