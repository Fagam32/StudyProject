package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class MainController {


    private final StationService stationService;

    @Autowired
    public MainController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/")
    public ModelAndView main() {

        ModelAndView modelAndView = new ModelAndView("index");
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stationList", allStations);

        return modelAndView;
    }

    @PostMapping("/")
    @Transactional
    public ModelAndView addStation(@RequestParam(name = "stationName") String stationName) {
        stationService.addStation(stationName);

        ModelAndView modelAndView = new ModelAndView("index");
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stationList", allStations);

        return modelAndView;
    }
}
