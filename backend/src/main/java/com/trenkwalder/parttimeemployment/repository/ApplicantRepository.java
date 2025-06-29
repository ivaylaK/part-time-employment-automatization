package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {

    Optional<Applicant> findByNumber(String number);
}
