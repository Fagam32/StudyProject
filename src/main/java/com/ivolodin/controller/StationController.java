package com.ivolodin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivolodin.model.dto.StationDto;
import com.ivolodin.model.dto.TrainDto;
import com.ivolodin.model.dto.View;
import com.ivolodin.services.StationService;
import com.ivolodin.services.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @Autowired
    private TrainService trainService;

    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public StationDto addNewStation(@Valid @RequestBody StationDto stationDto) {
        return stationService.addStation(stationDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{name}")
    public StationDto updateStation(@Valid StationDto oldStation,
                                    @Valid @RequestBody StationDto newStation) {
        return stationService.updateStation(oldStation, newStation);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{name}")
    public void deleteStation(@Valid StationDto station) {
        stationService.remove(station);
    }


    @GetMapping(params = {"name"})
    public List<StationDto> searchByName(@RequestParam("name") String stationName) {
        return stationService.getStationsByName(stationName);
    }

    @JsonView(View.Public.class)
    @GetMapping(value = "{stationName}", params = {"date"})
    public List<TrainDto> getTrainsOnDate(@PathVariable String stationName,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return trainService.getAllTrainsOnStation(stationName, date);
    }
}

