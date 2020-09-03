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


    @JsonView(View.Public.class)
    @GetMapping(value = "{name}", params = {"date"})
    public List<TrainDto> getTrainsOnDate(@Valid StationDto stationDto,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return trainService.getTrainsOnStation(stationDto, date);
    }
}
