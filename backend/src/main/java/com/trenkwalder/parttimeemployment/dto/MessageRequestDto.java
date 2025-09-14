package com.trenkwalder.parttimeemployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String message;
    private List<String> numbers;
}
