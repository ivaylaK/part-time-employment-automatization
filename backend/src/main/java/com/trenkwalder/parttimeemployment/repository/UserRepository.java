package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query()
    Optional<User> findByEmail(String email);
}
