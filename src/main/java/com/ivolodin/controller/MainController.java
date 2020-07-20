package com.ivolodin.controller;

import com.ivolodin.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

@Controller
public class MainController {


    private StationService stationService;

    @Autowired
    public MainController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @PostMapping("add")
    @Transactional
    public String addStation(@RequestParam(name = "stationName") String stationName) {
        stationService.addStation(stationName);
        return "index";
    }
}
