package com.ivolodin.services;

import com.ivolodin.entities.Station;
import com.ivolodin.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StationService {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private GraphService graphService;

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station addStation(Station newSt) {
        graphService.addVertex(newSt);
        return stationRepository.save(newSt);
    }

    public Station updateStation(Station oldSt, Station newSt) {
        oldSt.setName(newSt.getName());
        return stationRepository.save(oldSt);
    }

    public void remove(Station st){
        graphService.deleteVertex(st);

        stationRepository.delete(st);
    }

    public List<Station> getStationsByName(String stationName) {
        return stationRepository.getByNameContaining(stationName);
    }
}
