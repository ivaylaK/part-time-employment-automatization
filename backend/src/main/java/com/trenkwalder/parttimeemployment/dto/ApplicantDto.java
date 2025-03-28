package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDto {

    private Long id;

    private String firstName;
    private String lastName;
    private String number;
}
