package com.ivolodin.controller;

import com.ivolodin.dto.TrainDto;
import com.ivolodin.dto.TrainEdgeDto;
import com.ivolodin.services.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("{trainName}")
    public TrainDto getTrainInfo(TrainDto trainDto){
        return trainService.getTrainInfo(trainDto);
    }

    @PostMapping
    public TrainDto addNewTrain(@Valid @RequestBody TrainDto trainDto){
        return trainService.addNewTrain(trainDto);
    }

    @PutMapping
    public ResponseEntity<Object> updateTrainStandings(@RequestBody List<TrainEdgeDto> edgeDtos){
        trainService.updateStandings(edgeDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
