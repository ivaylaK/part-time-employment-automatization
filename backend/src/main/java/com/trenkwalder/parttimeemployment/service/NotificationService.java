package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.dto.NotificationDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.Notification;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.mapper.NotificationMapper;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.JobApplicationRepository;
import com.trenkwalder.parttimeemployment.repository.JobRepository;
import com.trenkwalder.parttimeemployment.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@EnableScheduling
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, ApplicantRepository applicantRepository, JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.notificationMapper = notificationMapper;
    }

    public Notification create(User user, String message) {

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);

        return notificationRepository.save(notification);
    }

    public Notification create(User user, NotificationDto notificationDto) {

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(notificationDto.getMessage());
        notification.setRead(false);
        notification.setCreated(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public List<NotificationDto> getAllByUser(User user) {
        return notificationRepository
                .findAllByUserAndCreatedAfterOrderByCreatedDesc(user, this.cleanUp())
                .stream()
                .map(notificationMapper::mapTo).toList();
    }

    public void markOneRead(Long id, User user) {
        Notification unreadNotification = notificationRepository.findByIdAndUserAndReadIsFalseAndCreatedAfter(id, user, this.cleanUp())
                .orElseThrow(() -> new IllegalArgumentException("Notification doesn't exist"));

        unreadNotification.setRead(true);
        notificationRepository.save(unreadNotification);
    }

    public void markAllRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByUserAndReadIsFalseAndCreatedAfterOrderByCreatedDesc(user, this.cleanUp());
        unreadNotifications.forEach(
                notification -> notification.setRead(true)
        );
        notificationRepository.saveAll(unreadNotifications);
    }

    public int unreadNotificationCount(User user) {
        return notificationRepository.countByUserAndReadIsFalseAndCreatedAfter(user, this.cleanUp());
    }

    @Transactional
    public void delete(Long id, User user) {
        Notification notification = notificationRepository.findByIdAndUserAndCreatedAfter(id, user, this.cleanUp())
                .orElseThrow(() -> new IllegalArgumentException("Notification doesn't exist"));

        notificationRepository.delete(notification);
    }

    private LocalDateTime cleanUp() {
        return LocalDateTime.now().minusDays(30);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpOldNotifications() {
        notificationRepository.deleteAllByCreatedBefore(cleanUp());
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void generateDeadlineReminders() {
        Date target = Date.valueOf(LocalDate.now().plusDays(10));
        LocalDateTime duplicateAfter = LocalDateTime.now().minusDays(11);

        List<Applicant> applicants = StreamSupport
                .stream(applicantRepository.findAll().spliterator(), false)
                .filter(applicant -> applicant.getUser() != null)
                .filter(applicant -> applicant.getUser().isEnabled())
                .filter(applicant -> applicant.getRank() != null && applicant.getRank() == 1)
                .toList();

        for (Applicant applicant : applicants) {
            String city = applicant.getCity();
            if (city == null) continue;

            List<Job> jobs = jobRepository.findCityJobsStartingBeforeTarget(city, target);
            for (Job job : jobs) {
                if (jobApplicationRepository.existsByApplicantAndJob(applicant, job)) continue;

                if (notificationRepository.findByUserAndJobAndCreatedAfter(applicant.getUser(), job, duplicateAfter).isPresent()) continue;

                Notification notification = new Notification();
                notification.setUser(applicant.getUser());
                notification.setMessage("There is a shift available in " + job.getCity() + ", " + job.getTitle() + ", " + job.getLocation() + ", " + job.getClient() + ". The application deadline is in 10 days.");
                notification.setJob(job);
                notification.setRead(false);
                notificationRepository.save(notification);
            }
        }
    }

}
