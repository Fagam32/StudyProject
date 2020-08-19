package com.ivolodin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class StationConnectDto {

    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 3, max = 45, message = "Name size must be between 3 and 45 characters")
    private String fromStation;

    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 3, max = 45, message = "Name size must be between 3 and 45 characters")
    private String toStation;

    @Positive(message = "Distance should be positive number")
    private Integer distance;
}
