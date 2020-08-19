package com.ivolodin.controller;

import com.ivolodin.dto.StationDto;
import com.ivolodin.entities.Station;
import com.ivolodin.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping
    public Station addNewStation(@RequestBody Station station) {
        return stationService.addStation(station);
    }

    @PutMapping("{name}")
    public StationDto updateStation(@Valid @PathVariable("name") StationDto oldStation,
                                    @Valid @RequestBody StationDto newStation) {
        return stationService.updateStation(oldStation, newStation);
    }

    @DeleteMapping("{name}")
    public void deleteStation(@Valid @PathVariable("name") StationDto station) {
        stationService.remove(station);
    }

    @GetMapping
    public List<StationDto> searchByName(@RequestParam("name") String stationName) {
        return stationService.getStationsByName(stationName);
    }
}
