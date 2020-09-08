package com.ivolodin.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class RegistrationDto {
    @NotBlank
    @Size(min = 3, max = 45, message = "Username length must be more than 3 and less than 45")
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 3)
    private String password;

    public void optimize(){
        name = name.trim();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        surname = surname.trim();
        surname = surname.substring(0, 1).toUpperCase() + surname.substring(1);
    }
}
