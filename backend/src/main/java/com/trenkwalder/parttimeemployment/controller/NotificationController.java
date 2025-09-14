package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.NotificationDto;
import com.trenkwalder.parttimeemployment.entity.Notification;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.mapper.NotificationMapper;
import com.trenkwalder.parttimeemployment.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @PostMapping()
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationDto notificationDto,
                                               @AuthenticationPrincipal User user) {
        Notification notification = notificationService.create(user, notificationDto);
        return new ResponseEntity<>(notificationMapper.mapTo(notification), HttpStatus.CREATED);
    }

    @GetMapping
    public List<NotificationDto> getAllByUser(@AuthenticationPrincipal User user) {
        return notificationService.getAllByUser(user);
    }

    @PostMapping(path = "/markOneRead/{id}")
    public ResponseEntity<Void> markOneRead(@PathVariable Long id,
                                            @AuthenticationPrincipal User user) {
        notificationService.markOneRead(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/markAllRead")
    public ResponseEntity<Void> markAllRead(@AuthenticationPrincipal User user) {
        notificationService.markAllRead(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/count")
    public int unreadNotificationCount(@AuthenticationPrincipal User user) {
        return notificationService.unreadNotificationCount(user);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        notificationService.delete(id, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
