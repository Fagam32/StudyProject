package com.ivolodin.services;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.repositories.EdgeRepository;
import com.ivolodin.repositories.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    private final AsSynchronizedGraph<Station, DefaultWeightedEdge> graph;

    private final StationRepository stationRepository;

    private final EdgeRepository edgeRepository;

    @Autowired
    public GraphService(StationRepository stRepo, EdgeRepository edgeRepo) {
        this.stationRepository = stRepo;
        this.edgeRepository = edgeRepo;

        graph = new AsSynchronizedGraph<>(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));

        List<Station> allStations = stRepo.findAll();
        for (Station s : allStations)
            graph.addVertex(s);

        List<StationConnect> allEdges = edgeRepo.findAll();
        for (StationConnect sc : allEdges) {
            DefaultWeightedEdge edge = graph.addEdge(sc.getFrom(), sc.getTo());
            graph.setEdgeWeight(edge, sc.getDistanceInMinutes());
        }
    }

    public List<Station> getPathList(Station from, Station to) {
        try {
            //It throws NPE if there's no way between Stations. So we have to catch it
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