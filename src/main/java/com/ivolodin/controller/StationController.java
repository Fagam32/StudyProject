package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StationController {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/addStation")
    public ModelAndView showStationList() {

        ModelAndView modelAndView = new ModelAndView("addStation");
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stationList", allStations);

        return modelAndView;
    }

    @PostMapping("/addStation")
    public ModelAndView addStation(@RequestParam(name = "stationName") String stationName) {
        stationService.addStation(stationName);

        ModelAndView modelAndView = new ModelAndView("addStation");
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stationList", allStations);

        return modelAndView;
    }

    @GetMapping("/addEdge")
    public ModelAndView showEdgeList() {
        ModelAndView modelAndView = new ModelAndView("addEdge");
        List<StationConnect> allEdges = stationService.getAllEdges();
        modelAndView.addObject("edgeList", allEdges);
        return modelAndView;
    }

    @PostMapping("/addEdge")
    public ModelAndView addEdge(@RequestParam(name = "frStat") String frStat,
                                @RequestParam(name = "toStat") String toStat,
                                @RequestParam(name = "distance") long distanceInMinutes) {

        stationService.addEdge(frStat, toStat, distanceInMinutes);

        ModelAndView modelAndView = new ModelAndView("addEdge");
        List<StationConnect> allEdges = stationService.getAllEdges();
        modelAndView.addObject("edgeList", allEdges);
        return modelAndView;

    }
}
