package com.ivolodin.services;

import com.ivolodin.dto.StationDto;
import com.ivolodin.entities.Station;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.utils.MapperUtils;
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

    public List<StationDto> getAllStations() {
        List<Station> allStations = stationRepository.findAll();
        return MapperUtils.mapAll(allStations, StationDto.class);
    }

    public StationDto addStation(StationDto newSt) {
        Station stEntity = MapperUtils.map(newSt, Station.class);
        stationRepository.save(stEntity);
        graphService.addVertex(stEntity);
        return newSt;
    }

    public StationDto updateStation(StationDto oldSt, StationDto newSt) {
        Station st = stationRepository.findByName(oldSt.getName());
        st.setName(newSt.getName());
        stationRepository.save(st);
        return MapperUtils.map(st, StationDto.class);
    }

    public void remove(StationDto dto) {
        Station st = stationRepository.findByName(dto.getName());
        if (st != null) {
            graphService.deleteVertex(st);
            stationRepository.delete(st);
        }
    }

    public List<StationDto> getStationsByName(String stationName) {
        List<Station> stationList = stationRepository.findByNameContaining(stationName);
        return MapperUtils.mapAll(stationList, StationDto.class);
    }

}
