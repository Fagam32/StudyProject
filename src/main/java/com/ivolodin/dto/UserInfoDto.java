package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserInfoDto {
    @JsonView(View.Public.class)
    private String userName;

    @JsonView(View.Private.class)
    private String name;

    @JsonView(View.Private.class)
    private String surname;

    @JsonView(View.Private.class)
    private LocalDate birthday;

    @JsonView(View.Public.class)
    private List<String> roles;

    @JsonView(View.Public.class)
    private String jwtToken;
    
    @JsonView(View.Private.class)
    private List<TicketDto> tickets;
}
