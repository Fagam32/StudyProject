package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.services.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController("edges")
public class EdgeController {

    @Autowired
    private EdgeService edgeService;

    @GetMapping("/edges")
    public List<StationConnect> getAllConnects() {
        return edgeService.getAll();
    }

    @GetMapping("{id}")
    public StationConnect getOneConnect(@PathVariable("id") StationConnect sc) {
        return sc;
    }

    @PostMapping
    public StationConnect addNewConnect(@RequestBody Station fromStation,
                                        @RequestBody Station toStation,
                                        @RequestBody int distance) {
        return edgeService.addNewEdge(fromStation, toStation, distance);
    }

    @PutMapping("{id}")
    public StationConnect editConnect(@PathVariable("id") StationConnect oldSc,
                                      @RequestBody StationConnect newSc) {
        return edgeService.update(oldSc, newSc);
    }

    @DeleteMapping("{id}")
    public void deleteConnect(@PathVariable("id") StationConnect sc) {
        edgeService.removeEdge(sc);
    }


}
