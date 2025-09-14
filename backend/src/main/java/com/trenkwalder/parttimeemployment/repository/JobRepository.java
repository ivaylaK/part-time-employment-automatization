package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    @Query("""
        SELECT job 
        FROM Job job 
        WHERE job.applicantsCount < job.totalSlots
    """)
    List<Job> findJobsWithAvailableSlots();

    @Query("""
      SELECT job FROM Job job
      WHERE lower(job.city) = lower(:city)
        AND job.applicantsCount < job.totalSlots
        AND job.startDate = :targetDate
    """)
    List<Job> findCityJobsStartingBeforeTarget(String city, Date targetDate);

    @Query("""
        SELECT DISTINCT lower(job.client)
        FROM Job job
        WHERE job.client IS NOT NULL 
            AND job.client <> '' 
    """)
    List<String> findDistinctClients();
}
