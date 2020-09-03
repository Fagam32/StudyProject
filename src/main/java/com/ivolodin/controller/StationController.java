package com.ivolodin.controller;

import com.ivolodin.dto.StationDto;
import com.ivolodin.dto.TrainDto;
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
    public StationDto addNewStation(@Valid @RequestBody StationDto stationDto) {
        return stationService.addStation(stationDto);
    }

    @PutMapping("{name}")
    public StationDto updateStation(@Valid StationDto oldStation,
                                    @Valid @RequestBody StationDto newStation) {
        return stationService.updateStation(oldStation, newStation);
    }

    @DeleteMapping("{name}")
    public void deleteStation(@Valid StationDto station) {
        stationService.remove(station);
    }

    @GetMapping(params = {"name"})
    public List<StationDto> searchByName(@RequestParam("name") String stationName) {
        return stationService.getStationsByName(stationName);
    }
}

