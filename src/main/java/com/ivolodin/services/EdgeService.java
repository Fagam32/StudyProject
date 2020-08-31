package com.ivolodin.services;

import com.ivolodin.dto.StationConnectDto;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.repositories.EdgeRepository;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional
@Service
public class EdgeService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private GraphService graphService;

    public StationConnectDto addNewEdge(StationConnectDto newSc) {
        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());

        checkConnectExisting(newSc, frSt, toSt);
        graphService.addEdge(frSt, toSt, newSc.getDistance());

        StationConnect scEntity = new StationConnect();
        scEntity.setFrom(frSt);
        scEntity.setTo(toSt);
        scEntity.setDistanceInMinutes(newSc.getDistance());

        StationConnect saved = edgeRepository.save(scEntity);
        return MapperUtils.map(saved, StationConnectDto.class);
    }

    public void removeEdge(StationConnectDto sc) {
        //TODO check if there are trains passing through this

        Station frSt = stationRepository.findByName(sc.getFromStation());
        Station toSt = stationRepository.findByName(sc.getToStation());

        checkConnectExisting(sc, frSt, toSt);

        edgeRepository.deleteByFromAndTo(frSt, toSt);

        graphService.deleteEdge(frSt, toSt);
    }

    public List<StationConnectDto> getAll() {
        return MapperUtils.mapAll(edgeRepository.findAll(), StationConnectDto.class);
    }

    public StationConnectDto update(StationConnectDto newSc) {
        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());

        checkConnectExisting(newSc, frSt, toSt);

        StationConnect connect = edgeRepository.findByFromAndTo(frSt, toSt);
        connect.setDistanceInMinutes(newSc.getDistance());
        edgeRepository.save(connect);

        graphService.updateEdge(connect.getFrom(), connect.getTo(), connect.getDistanceInMinutes());

        return MapperUtils.map(connect, StationConnectDto.class);
    }

    public StationConnect getStationConnect(Station fr, Station to){
       return edgeRepository.findByFromAndTo(fr, to);
    }

    private void checkConnectExisting(StationConnectDto newSc, Station frSt, Station toSt) {
        if (frSt == null || toSt == null) {
            if (frSt == null)
                throw new EntityNotFoundException("Station with name " + newSc.getFromStation() + " not found");
            else
                throw new EntityNotFoundException("Station with name " + newSc.getToStation() + " not found");
        }
    }


    public Integer getDistanceBetweenStations(Station fr, Station to) {
        return edgeRepository.findByFromAndTo(fr, to).getDistanceInMinutes();
    }
}
