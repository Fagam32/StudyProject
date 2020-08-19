package com.ivolodin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class StationDto {
    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 3, max = 45, message = "Name size must be between 3 and 45 characters")
    private String name;
}
