package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.ApplicantDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicantMapper implements Mapper<Applicant, ApplicantDto> {

    private final ModelMapper modelMapper;

    public ApplicantMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Applicant mapFrom(ApplicantDto applicantDto) {
        return modelMapper.map(applicantDto, Applicant.class);
    }

    @Override
    public ApplicantDto mapTo(Applicant applicant) {
        return modelMapper.map(applicant, ApplicantDto.class);
    }
}
