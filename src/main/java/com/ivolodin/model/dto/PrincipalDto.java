package com.ivolodin.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PrincipalDto {
    private String jwtToken;
    private List<String> roles;
}
