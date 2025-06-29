package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.JobDto;
import com.trenkwalder.parttimeemployment.entity.Job;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class JobMapper implements Mapper<Job, JobDto> {

    private final ModelMapper modelMapper;

    public JobMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        Converter<String, LocalTime> stringToLocalTime = new Converter<>() {

            @Override
            public LocalTime convert(MappingContext<String, LocalTime> context) {
                return context.getSource() == null ? null : LocalTime.parse(context.getSource());
            }
        };

        Converter<LocalTime, String> localTimeToString = new Converter<>() {

            @Override
            public String convert(MappingContext<LocalTime, String> context) {
                return context.getSource() == null ? null : context.getSource().toString();
            }
        };

        modelMapper.typeMap(JobDto.class, Job.class).addMappings(mapper -> {
            mapper.using(stringToLocalTime).map(JobDto::getShiftStart, Job::setShiftStart);
            mapper.using(stringToLocalTime).map(JobDto::getShiftEnd, Job::setShiftEnd);
        });

        modelMapper.typeMap(Job.class, JobDto.class).addMappings(mapper -> {
            mapper.using(localTimeToString).map(Job::getShiftStart, JobDto::setShiftStart);
            mapper.using(localTimeToString).map(Job::getShiftEnd, JobDto::setShiftEnd);
        });
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
