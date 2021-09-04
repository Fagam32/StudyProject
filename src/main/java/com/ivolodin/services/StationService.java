package com.ivolodin.services;

import com.ivolodin.model.dto.StationDto;
import com.ivolodin.model.entities.Station;
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

    private StationRepository stationRepository;

    private TrainEdgeRepository trainEdgeRepository;

    private GraphService graphService;

    /**
     * @return list of stationDto with all stations
     */
    public List<StationDto> getAllStations() {
        List<Station> allStations = stationRepository.findAll();
        return MapperUtils.mapAll(allStations, StationDto.class);
    }


    /**
     * Adds new Station to database and graph
     *
     * @param stationName the name of new Station
     * @return stationDto of newly created station
     */
    public StationDto addStation(String stationName) {
        stationName = stationName.trim();

        if (stationName.length() < 3)
            throw new IllegalArgumentException("Station should contain at least 3 symbols");

        if (stationRepository.existsByName(stationName))
            throw new IllegalArgumentException("Station with such name already exists");

        Station station = new Station(stationName);

        stationRepository.save(station);
        graphService.addVertex(station);
        StationDto stationDto = MapperUtils.map(station, StationDto.class);
        log.info("Station {} added", stationDto.toString());
        return stationDto;
    }

    /**
     * Adds new Station to database and graph
     *
     * @param newSt the StationDto containing name of new Station
     * @return stationDto of newly created station
     */
    public StationDto addStation(StationDto newSt) {
        return addStation(newSt.getName());
    }


    /**
     * Updates name of Station
     *
     * @param oldSt StationDto with old station name
     * @param newSt StationDto with new station name
     * @return StationDto with updated station
     */
    public StationDto updateStation(StationDto oldSt, StationDto newSt) {
        Station st = stationRepository.findByName(oldSt.getName());
        st.setName(newSt.getName());
        stationRepository.save(st);
        log.info("Station {} updated", newSt.toString());
        return MapperUtils.map(st, StationDto.class);
    }


    /**
     * Removes Station from database and graph
     *
     * @param oldSt StationDto with station name to remove
     * @throws IllegalArgumentException if there's at least one train passing through this station
     */
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

    /**
     * Method for auto-suggest component in front layer
     *
     * @param stationName part of station name
     * @return list of all stations containing stationName
     */
    public List<StationDto> getStationsByName(String stationName) {
        List<Station> stationList = stationRepository.findByNameContaining(stationName);
        return MapperUtils.mapAll(stationList, StationDto.class);
    }

    @Autowired
    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Autowired
    public void setTrainEdgeRepository(TrainEdgeRepository trainEdgeRepository) {
        this.trainEdgeRepository = trainEdgeRepository;
    }

    @Autowired
    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }
}
