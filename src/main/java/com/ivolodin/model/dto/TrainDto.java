package com.ivolodin.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDto {

    @JsonView(View.Public.class)
    @NotBlank
    private String trainName;

    @JsonView(View.Public.class)
    @Positive
    private Integer seatsNumber;

    @JsonView(View.Public.class)
    @NotBlank
    @Size(min = 3, max = 45)
    private String fromStation;

    @JsonView(View.Public.class)
    @NotBlank
    @Size(min = 3, max = 45)
    private String toStation;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @FutureOrPresent
    private LocalDateTime departure;

    @JsonView(View.Public.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    @JsonView(View.Public.class)
    private List<TrainEdgeDto> path;

}
