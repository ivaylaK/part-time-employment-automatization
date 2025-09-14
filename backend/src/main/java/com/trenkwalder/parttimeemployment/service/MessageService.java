package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.entity.Applicant;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.*;

@Service
public class MessageService {

    @Value("${mobica.smsApi}")
    private String mobicaSmsApi;

    @Value("${mobica.viberApi}")
    private String mobicaViberApi;

    @Value("${mobica.username}")
    private String mobicaUsername;

    @Value("${mobica.password}")
    private String mobicaPassword;

    private final RestTemplate restTemplate;
    private final ApplicantService applicantService;

    public MessageService(RestTemplate restTemplate, ApplicantService applicantService) {
        this.restTemplate = restTemplate;
        this.applicantService = applicantService;
    }

    public void sendSms(String message, List<String> numbers) {
        Map<String, Object> base = new HashMap<>();
        base.put("user", mobicaUsername);
        base.put("pass", mobicaPassword);

        List<Map<String, String>> smsList = new ArrayList<>();
        for (String number : numbers) {
            Map<String, String> smsDetails = new HashMap<>();
            smsDetails.put("idd", UUID.randomUUID().toString());
            smsDetails.put("phone", number);
            smsDetails.put("message", message);
            smsList.add(smsDetails);
        }
        base.put("sms", smsList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(base, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(mobicaSmsApi, httpEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Couldn't send the message: " + responseEntity.getBody());
        }
    }

    public void sendViberMessage(String message, List<String> numbers) {
        Map<String, Object> base = new HashMap<>();
        base.put("user", mobicaUsername);
        base.put("pass", mobicaPassword);

        List<Map<String, Object>> messageList = new ArrayList<>();
        for (String number : numbers) {
            Map<String, Object> messageDetails = new HashMap<>();
            messageDetails.put("idd", UUID.randomUUID().toString());
            messageDetails.put("tag", "Tag Text");
            messageDetails.put("phone", number);
            messageDetails.put("text", message);
            messageDetails.put("image_url", "");
            messageDetails.put("button_url", "");
            messageDetails.put("button_txt", "");
            messageDetails.put("is_promotional", 0);
            messageDetails.put("validity_period_sec", 600);
            messageDetails.put("sms_text", "Message failed");
            messageList.add(messageDetails);
        }
        base.put("viber", messageList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(base, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(mobicaViberApi, httpEntity, String.class);
        System.out.println("Response from Viber API: " + responseEntity.getBody());

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Couldn't send viber message: " + responseEntity.getBody());
        }
    }

    public void sendSmsToOneApplicant(String message, String number) {
        sendSms(message, Collections.singletonList(number));
    }

    public void sendViberMessageToOneApplicant(String message, String number) {
        sendViberMessage(message, Collections.singletonList(number));
    }

    @Async
    public void sendSmsToAllApplicants(String message) {
        List<Applicant> applicants = applicantService.findAllApplicants();

        List<String> numbers = applicants.stream()
                .map(Applicant::getNumber).toList();

        if (!numbers.isEmpty()) {
            sendSms(message, numbers);
        }
    }

    @Async
    public void sendViberMessageToAllApplicants(String message) {
        List<Applicant> applicants = applicantService.findAllApplicants();

        List<String> numbers = applicants.stream()
                .map(Applicant::getNumber).toList();

        if (!numbers.isEmpty()) {
            sendViberMessage(message, numbers);
        }
    }
}
