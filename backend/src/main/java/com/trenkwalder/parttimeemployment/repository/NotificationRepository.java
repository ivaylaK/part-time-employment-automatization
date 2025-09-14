package com.trenkwalder.parttimeemployment.repository;

import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.Notification;
import com.trenkwalder.parttimeemployment.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Optional<Notification> findByIdAndUserAndCreatedAfter(Long id, User user, LocalDateTime cleanUp);

    List<Notification> findAllByUserAndCreatedAfterOrderByCreatedDesc(User user, LocalDateTime cleanUp);

    Optional<Notification> findByIdAndUserAndReadIsFalseAndCreatedAfter(Long id, User user, LocalDateTime cleanUp);

    List<Notification> findByUserAndReadIsFalseAndCreatedAfterOrderByCreatedDesc(User user, LocalDateTime cleanUp);

    Optional<Notification> findByUserAndJobAndCreatedAfter(User user, Job job, LocalDateTime cleanUp);

    int countByUserAndReadIsFalseAndCreatedAfter(User user, LocalDateTime cleanUp);

    void deleteAllByCreatedBefore(LocalDateTime cleanUp);

    List<Notification> findAllByJob(Job job);

    void deleteAllByJob(Job job);
}
