package com.ivolodin.services;

import com.ivolodin.dto.StationDto;
import com.ivolodin.entities.Station;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainEdgeRepository;
import com.ivolodin.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class StationService {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainEdgeRepository trainEdgeRepository;
    @Autowired
    private GraphService graphService;

    public List<StationDto> getAllStations() {
        List<Station> allStations = stationRepository.findAll();
        return MapperUtils.mapAll(allStations, StationDto.class);
    }

    public StationDto addStation(StationDto newSt) {
        Station stEntity = MapperUtils.map(newSt, Station.class);
        stationRepository.save(stEntity);
        graphService.addVertex(stEntity);
        log.info("Station {} added", newSt.toString());
        return newSt;
    }

    public StationDto updateStation(StationDto oldSt, StationDto newSt) {
        Station st = stationRepository.findByName(oldSt.getName());
        st.setName(newSt.getName());
        stationRepository.save(st);
        log.info("Station {} updated", newSt.toString());
        return MapperUtils.map(st, StationDto.class);
    }

    public void remove(StationDto oldSt) {
        Station st = stationRepository.findByName(oldSt.getName());
        if (trainEdgeRepository.existsTrainEdgeByStation(st))
            throw new IllegalArgumentException("This station can't be deleted because there are trains passing through it");
        if (st != null) {
            graphService.deleteVertex(st);
            stationRepository.delete(st);
            log.info("Station {} deleted", oldSt.toString());
        }
    }

    public List<StationDto> getStationsByName(String stationName) {
        List<Station> stationList = stationRepository.findByNameContaining(stationName);
        return MapperUtils.mapAll(stationList, StationDto.class);
    }

}
