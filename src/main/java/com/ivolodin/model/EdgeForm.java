package com.ivolodin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class EdgeForm {

    @NotEmpty
    private String fromStation;

    @NotEmpty
    private String toStation;

    @Min(1)
    private Integer distance;
}
