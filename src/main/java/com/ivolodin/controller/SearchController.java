package com.ivolodin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivolodin.model.dto.TrainDto;
import com.ivolodin.model.dto.View;
import com.ivolodin.services.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private TrainService trainService;


    @JsonView(View.Public.class)
    @GetMapping(params = {"from", "to", "date"})
    public List<TrainDto> search(@RequestParam("from") String from,
                                 @RequestParam("to") String to,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return trainService.getTrainsInDate(from, to, date);
    }
}
