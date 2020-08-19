package com.ivolodin.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {
    private String userName;
    private String name;
    private String surname;
    private String email;
    private LocalDate birthday;
    private List<String> roles;
    private List<TicketDto> tickets;
}
