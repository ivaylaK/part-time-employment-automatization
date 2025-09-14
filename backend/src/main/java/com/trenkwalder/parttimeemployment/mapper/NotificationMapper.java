package com.trenkwalder.parttimeemployment.mapper;

import com.trenkwalder.parttimeemployment.dto.NotificationDto;
import com.trenkwalder.parttimeemployment.entity.Job;
import com.trenkwalder.parttimeemployment.entity.Notification;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class NotificationMapper {

    public NotificationDto mapTo(Notification notification) {

        NotificationDto notificationDto = new NotificationDto();

        notificationDto.setId(notification.getId());
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setRead(notification.isRead());
        notificationDto.setCreated(notification.getCreated());

        Job job = notification.getJob();
        if (job != null) {
            notificationDto.setJobId(job.getId());
            notificationDto.setJobTitle(job.getTitle());
            notificationDto.setJobClient(job.getClient());
            notificationDto.setJobCity(job.getCity());
            notificationDto.setJobLocation(job.getLocation());
            notificationDto.setJobStartDate(job.getStartDate());
            notificationDto.setJobEndDate(job.getEndDate());
        }
        return notificationDto;
    }
}
