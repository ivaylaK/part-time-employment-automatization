package com.trenkwalder.parttimeemployment.controller;

import com.trenkwalder.parttimeemployment.dto.RequestDto;
import com.trenkwalder.parttimeemployment.entity.Request;
import com.trenkwalder.parttimeemployment.mapper.RequestMapper;
import com.trenkwalder.parttimeemployment.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/application-requests")
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public RequestController(RequestService requestService, RequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping()
    public ResponseEntity<RequestDto> create(@RequestParam Long jobId,
                                             @RequestParam Long applicantId,
                                             @RequestParam String message,
                                             @RequestParam String type) {

        Request request = requestService.create(jobId, applicantId, message, type);
        return new ResponseEntity<>(requestMapper.mapTo(request), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<RequestDto> getAll() {
        List<Request> requests = requestService.findAll();
        return requests
                .stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approveAndDelete(@PathVariable("id") Long id) {
        requestService.approveAndDelete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        requestService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


