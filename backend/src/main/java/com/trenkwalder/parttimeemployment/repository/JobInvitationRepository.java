package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.JobInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobInvitationRepository extends CrudRepository<JobInvitation, Long> {

    Optional<JobInvitation> findByToken(String token);
}
