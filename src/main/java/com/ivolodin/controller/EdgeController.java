package com.ivolodin.controller;

import com.ivolodin.dto.StationConnectDto;
import com.ivolodin.services.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/edges")
public class EdgeController {

    @Autowired
    private EdgeService edgeService;

    @GetMapping("/edges")
    public List<StationConnectDto> getAllConnects() {
        return edgeService.getAll();
    }

    @PostMapping
    public StationConnectDto addNewConnect(@Valid StationConnectDto scDto) {
        return edgeService.addNewEdge(scDto);
    }

    @PutMapping
    public StationConnectDto editConnect(@Valid @RequestBody StationConnectDto newSc) {
        return edgeService.update(newSc);
    }

    @DeleteMapping
    public void deleteConnect(@Valid @RequestBody StationConnectDto sc) {
        edgeService.removeEdge(sc);
    }


}
