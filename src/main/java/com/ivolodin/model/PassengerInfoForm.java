package com.ivolodin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PassengerInfoForm {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Surname cannot be empty")
    private String surname;

    @NotNull
    @Past(message = "Are you from future?)")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
