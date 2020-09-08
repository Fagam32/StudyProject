package com.ivolodin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivolodin.dto.StationDto;
import com.ivolodin.dto.TrainDto;
import com.ivolodin.dto.TrainEdgeDto;
import com.ivolodin.dto.View;
import com.ivolodin.services.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping
    public List<TrainDto> getAllTrains() {
        return trainService.getAllTrains();
    }

    @JsonView(View.Public.class)
    @GetMapping("{trainName}")
    public TrainDto getTrainInfo(TrainDto trainDto){
        return trainService.getTrainInfo(trainDto);
    }

    @PostMapping
    public TrainDto addNewTrain(@Valid @RequestBody TrainDto trainDto){
        return trainService.addNewTrain(trainDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<Object> updateTrainStandings(@RequestBody List<TrainEdgeDto> edgeDtos){
        trainService.updateStandings(edgeDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{trainName}")
    public void deleteTrain(@PathVariable String trainName){
        trainService.deleteTrainByName(trainName);
    }
}
