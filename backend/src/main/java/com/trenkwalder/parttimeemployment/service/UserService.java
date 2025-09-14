package com.trenkwalder.parttimeemployment.service;

import com.trenkwalder.parttimeemployment.dto.LoginDto;
import com.trenkwalder.parttimeemployment.dto.UserDto;
import com.trenkwalder.parttimeemployment.entity.Applicant;
import com.trenkwalder.parttimeemployment.entity.User;
import com.trenkwalder.parttimeemployment.repository.ApplicantRepository;
import com.trenkwalder.parttimeemployment.repository.UserRepository;
import com.trenkwalder.parttimeemployment.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, ApplicantRepository applicantRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User signUp(UserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use. Log in instead");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        User saved = userRepository.save(user);

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            Applicant applicant = new Applicant();

            applicant.setFirstName(dto.getFirstName());
            applicant.setLastName(dto.getLastName());
            applicant.setNumber(dto.getNumber());
            applicant.setCity(dto.getCity());
            applicant.setRank(0);

            applicant.setUser(saved);
            applicantRepository.save(applicant);
        }

        return saved;
    }

    public String signIn(LoginDto dto) {
        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password!")
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }
        System.out.println(user.getRole());
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
