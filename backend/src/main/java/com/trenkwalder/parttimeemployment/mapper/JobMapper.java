package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.JobDto;
import com.trenkwalder.parttimeemployment.entity.Job;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JobMapper implements Mapper<Job, JobDto> {

    private final ModelMapper modelMapper;

    public JobMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Job mapFrom(JobDto jobDto) {
        return modelMapper.map(jobDto, Job.class);
    }

    @Override
    public JobDto mapTo(Job job) {
        return modelMapper.map(job, JobDto.class);
    }
}
