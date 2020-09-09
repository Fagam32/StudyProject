package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketDto {

    @JsonView(View.Private.class)
    private String name;

    @JsonView(View.Private.class)
    private String surname;

    @JsonView(View.Private.class)
    private LocalDate birthdate;

    @NotEmpty
    @JsonView(View.Private.class)
    private String trainName;

    @JsonView(View.Private.class)
    @NotEmpty
    @Size(min = 3, max = 45)
    private String fromStation;

    @JsonView(View.Private.class)
    @NotEmpty
    @Size(min = 3, max = 45)
    private String toStation;

    @JsonView(View.Private.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @JsonView(View.Private.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;
}
