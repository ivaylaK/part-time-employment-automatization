package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Long> {

    Optional<Applicant> findByNumber(String number);

    Optional<Applicant> findByUser(User user);
}
