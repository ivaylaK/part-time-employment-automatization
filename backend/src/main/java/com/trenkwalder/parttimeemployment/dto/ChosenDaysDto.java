package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChosenDaysDto {

    private List<Date> chosenDays;
}
