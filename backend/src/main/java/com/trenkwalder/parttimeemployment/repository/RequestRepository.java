package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

}
