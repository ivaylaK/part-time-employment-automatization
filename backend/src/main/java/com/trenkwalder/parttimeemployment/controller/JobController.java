package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.JobDto;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.mapper.Mapper;
import com.trenkwalder.parttimeemployment.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class JobController {

    private final JobService jobService;
    private final Mapper<Job, JobDto> jobMapper;


    public JobController(JobService jobService, Mapper<Job, JobDto> jobMapper) {
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }

    @PostMapping(path = "/jobs")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        Job job = jobService.saveJob(jobMapper.mapFrom(jobDto));
        return new ResponseEntity<>(jobMapper.mapTo(job), HttpStatus.CREATED);

    }

    @GetMapping(path = "/jobs")
    public List<JobDto> getAllJobs() {
        List<Job> jobs = jobService.findAllJobs();
        return jobs
                .stream()
                .map(jobMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/jobs/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable("id") Long id) {
        Optional<Job> foundJob = jobService.findJobById(id);
        return foundJob.map(
                job -> new ResponseEntity<>(jobMapper.mapTo(job), HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/jobs/{id}")
    public ResponseEntity<JobDto> updateJob(@RequestBody JobDto jobDto, @PathVariable("id") Long id) {
        if (jobService.findJobById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Job updatedJob = jobMapper.mapFrom(jobDto);
        updatedJob.setId(id);
        jobService.saveJob(updatedJob);
        return new ResponseEntity<>(jobMapper.mapTo(updatedJob), HttpStatus.OK);
    }


    @DeleteMapping(path = "/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id) {
        if (jobService.findJobById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
