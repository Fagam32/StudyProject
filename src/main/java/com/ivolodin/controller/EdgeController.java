package com.ivolodin.controller;

import com.ivolodin.dto.StationConnectDto;
import com.ivolodin.services.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/edges")
public class EdgeController {

    @Autowired
    private EdgeService edgeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<StationConnectDto> getAllConnects() {
        return edgeService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public StationConnectDto addNewConnect(@Valid @RequestBody StationConnectDto scDto) {
        return edgeService.addNewEdge(scDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public StationConnectDto editConnect(@Valid @RequestBody StationConnectDto newSc) {
        return edgeService.update(newSc);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{fromStation}/{toStation}")
    public void deleteConnect(@Valid StationConnectDto sc) {
        edgeService.removeEdge(sc);
    }
}
