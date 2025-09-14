package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.RequestDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Request;
import com.trenkwalder.parttimeemployment.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestDto mapTo(Request request) {

        Applicant applicant = request.getApplicant();
        Job job = request.getJob();

        RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());

        if (applicant != null) {
            requestDto.setApplicantId(applicant.getId());
            requestDto.setApplicantFirstName(applicant.getFirstName());
            requestDto.setApplicantLastName(applicant.getLastName());
            requestDto.setApplicantNumber(applicant.getNumber());
            requestDto.setApplicantCity(applicant.getCity());
            requestDto.setApplicantRank(applicant.getRank());
        }

        if (job != null) {
            requestDto.setJobId(job.getId());
            requestDto.setJobTitle(job.getTitle());
            requestDto.setJobClient(job.getClient());
            requestDto.setJobCity(job.getCity());
            requestDto.setJobLocation(job.getLocation());
            requestDto.setJobStoreNumber(job.getStoreNumber());
            requestDto.setJobStartDate(job.getStartDate());
            requestDto.setJobEndDate(job.getEndDate());
        }

        requestDto.setMessage(request.getMessage());
        requestDto.setSent(request.getSent());

        return requestDto;
    }
}
