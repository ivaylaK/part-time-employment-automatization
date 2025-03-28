package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {
}
