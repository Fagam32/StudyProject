package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController("stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping(path = "/stations")
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("{id}")
    public Station getOneStation(@PathVariable("id") Station station) {
        return station;
    }

    @PostMapping("/stations")
    public Station addNewStation(@RequestBody Station station) {
        return stationService.addStation(station);
    }

    @PutMapping("{id}")
    public Station updateStation(@PathVariable("id") Station oldStation,
                                 @RequestBody Station newStation) {
        return stationService.updateStation(oldStation, newStation);
    }

    @DeleteMapping("{id}")
    public void deleteStation(@PathVariable("id") Station station) {
        stationService.remove(station);
    }
}
