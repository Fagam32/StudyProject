package com.ivolodin.services;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.repositories.EdgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EdgeService {

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private GraphService graphService;

    public StationConnect addNewEdge(Station stFr, Station stTo, long distance){
        StationConnect newStConnect = new StationConnect();
        newStConnect.setFrom(stFr);
        newStConnect.setTo(stTo);
        newStConnect.setDistanceInMinutes(distance);

        graphService.addEdge(stFr, stTo, distance);

        return edgeRepository.save(newStConnect);
    }

    public void removeEdge(StationConnect sc){
        //TODO check if there are trains passing through this
        edgeRepository.delete(sc);

        graphService.updateEdge(sc.getFrom(), sc.getTo(), sc.getDistanceInMinutes());
    }

    public List<StationConnect> getAll() {
        return edgeRepository.findAll();
    }

    public StationConnect update(StationConnect oldSc, StationConnect newSc) {
        oldSc.setFrom(newSc.getFrom());
        oldSc.setTo(newSc.getTo());
        oldSc.setDistanceInMinutes(newSc.getDistanceInMinutes());

        graphService.updateEdge(newSc.getFrom(), newSc.getTo(), newSc.getDistanceInMinutes());

        return edgeRepository.save(oldSc);
    }
}
