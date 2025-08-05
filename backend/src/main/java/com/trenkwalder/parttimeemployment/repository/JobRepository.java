package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    @Query("SELECT job FROM Job job WHERE job.applicantsCount < job.totalSlots")
    List<Job> findJobsWithAvailableSlots();
}
