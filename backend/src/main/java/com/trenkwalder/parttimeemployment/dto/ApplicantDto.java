package com.trenkwalder.parttimeemployment.dto;

import com.trenkwalder.parttimeemployment.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDto {

    private Long id;

    private String firstName;
    private String lastName;
    private String number;
    private String city;

    private List<Job> jobsApplied = new ArrayList<>();
    private Integer rank;
}
