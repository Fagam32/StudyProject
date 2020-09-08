package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoDto {
    @JsonView(View.Public.class)
    private String userName;

    @JsonView(View.Public.class)
    private String name;

    @JsonView(View.Public.class)
    private String surname;

    @JsonView(View.Public.class)
    private LocalDate birthdate;

    @JsonView(View.Private.class)
    private String email;

}
