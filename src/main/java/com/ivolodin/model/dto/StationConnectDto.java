package com.ivolodin.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class StationConnectDto {

    @JsonView(View.Public.class)
    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 3, max = 45, message = "Name size must be between 3 and 45 characters")
    private String fromStation;

    @JsonView(View.Public.class)
    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 3, max = 45, message = "Name size must be between 3 and 45 characters")
    private String toStation;

    @JsonView(View.Public.class)
    @Positive(message = "Distance should be positive number")
    private Integer distance;
}
