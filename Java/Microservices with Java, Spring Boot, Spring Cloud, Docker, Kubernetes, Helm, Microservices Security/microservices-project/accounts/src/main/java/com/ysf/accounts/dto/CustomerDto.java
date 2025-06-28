package com.ysf.accounts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDto {

    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 30, message = "Name should contain 5-30 characters")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid Email address format")
    private String email;

    @NotEmpty(message = "Mobile number cannot be empty")
    private String mobileNumber;
}
