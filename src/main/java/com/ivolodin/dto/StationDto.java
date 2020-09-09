package com.ivolodin.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class StationDto {

    @JsonView(View.Public.class)
    @NotBlank(message = "Station name cannot be empty")
    @Size(min = 2, max = 45, message = "Name size must be between 3 and 45 characters")
    private String name;
}
