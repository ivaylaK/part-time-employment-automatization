package com.trenkwalder.parttimeemployment.dto;

import com.trenkwalder.parttimeemployment.dto.validationGroups.OnAdminCreation;
import com.trenkwalder.parttimeemployment.dto.validationGroups.OnUserRegistration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email
    @NotNull(groups = {OnAdminCreation.class, OnUserRegistration.class})
    private String email;

    @NotBlank(groups = {OnAdminCreation.class, OnUserRegistration.class})
    private String password;

    private String role;

    @NotBlank(groups = OnUserRegistration.class)
    private String firstName;

    @NotBlank(groups = OnUserRegistration.class)
    private String lastName;

    @NotBlank(groups = OnUserRegistration.class)
    private String number;

    @NotBlank(groups = OnUserRegistration.class)
    private String city;
}
