package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import com.ivolodin.service.StationService;
import com.ivolodin.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @Autowired
    private TrainService trainService;

    @GetMapping
    public ModelAndView getAllStations() {
        ModelAndView modelAndView = new ModelAndView("stations");
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stations", allStations);
        return modelAndView;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(params = "stationName")
    public String addStation(@NotNull @RequestParam String stationName) {
        stationService.addStation(stationName);
        return "redirect:/stations";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(params = "stationId")
    public String deleteStation(@NotNull @RequestParam Integer stationId) {
        stationService.deleteStation(stationId);
        return "redirect:/stations";
    }

    @GetMapping("/{stationName}/trains")
    public ModelAndView showTrainsOnStation(@PathVariable String stationName) {
        ModelAndView modelAndView = new ModelAndView("trainsOnStation");
        List<Train> trains = trainService.getTrainsOnStation(stationName);
        modelAndView.addObject("trains", trains);
        return modelAndView;
    }
}
