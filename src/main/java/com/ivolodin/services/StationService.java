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

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station addStation(Station newSt) {
        return stationRepository.save(newSt);
    }

    public Station updateStation(Station oldSt, Station newSt) {
        oldSt.setName(newSt.getName());
        return stationRepository.save(oldSt);
    }

    public void remove(Station st){
        stationRepository.delete(st);
    }
}
