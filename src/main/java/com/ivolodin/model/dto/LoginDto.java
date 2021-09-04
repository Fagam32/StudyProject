package com.ivolodin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotBlank
    @Size(min = 3)
    private String username;

    @NotBlank
    @Size(min = 3)
    private String password;

}
