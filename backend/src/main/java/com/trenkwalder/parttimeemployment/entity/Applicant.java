package com.trenkwalder.parttimeemployment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
