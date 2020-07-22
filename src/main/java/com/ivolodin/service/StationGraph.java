package com.ivolodin.service;

import com.ivolodin.dao.StationConnectDao;
import com.ivolodin.dao.StationDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationGraph {
    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> graph;

    private final StationDao stationDao;

    private final StationConnectDao stationConnectDao;

    @Autowired
    public StationGraph(StationDao stationDao, StationConnectDao stationConnectDao) {

        this.stationDao = stationDao;

        this.stationConnectDao = stationConnectDao;

        graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        List<Station> allStations = stationDao.getAll();
        for (Station s : allStations)
            graph.addVertex(s);

        List<StationConnect> allEdges = stationConnectDao.getAll();
        for (StationConnect sc : allEdges) {
            DefaultWeightedEdge edge = graph.addEdge(sc.getFrom(), sc.getTo());
            graph.setEdgeWeight(edge, sc.getDistanceInMinutes());
        }
    }

    public List<Station> getPathList(Station from, Station to) {
        try {
            return DijkstraShortestPath.findPathBetween(graph, from, to).getVertexList();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void addEdge(Station fr, Station to, long dist) {
        DefaultWeightedEdge edge = graph.addEdge(fr, to);
        graph.setEdgeWeight(edge, dist);
    }

    public void updateEdge(Station fr, Station to, long dist) {
        DefaultWeightedEdge edge = graph.getEdge(fr, to);
        graph.setEdgeWeight(edge, dist);
    }

    public void addVertex(Station station) {
        graph.addVertex(station);
    }

    public void deleteVertex(Station station) {
        graph.removeVertex(station);
    }

    public void deleteEdge(Station fr, Station to) {
        graph.removeEdge(fr, to);
    }
}
