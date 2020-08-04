package com.ivolodin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@NoArgsConstructor
public class SearchForm {

    @NotEmpty
    private String stationFrom;

    @NotEmpty
    private String stationTo;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
