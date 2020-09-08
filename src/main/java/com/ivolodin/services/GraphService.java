package com.ivolodin.services;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.repositories.EdgeRepository;
import com.ivolodin.repositories.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GraphService {

    private final AsSynchronizedGraph<Station, DefaultWeightedEdge> graph;

    @Autowired
    public GraphService(StationRepository stRepo, EdgeRepository edgeRepo) {

        graph = new AsSynchronizedGraph<>(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));

        List<Station> allStations = stRepo.findAll();
        for (Station s : allStations)
            graph.addVertex(s);

        List<StationConnect> allEdges = edgeRepo.findAll();
        for (StationConnect sc : allEdges) {
            DefaultWeightedEdge edge = graph.addEdge(sc.getFrom(), sc.getTo());
            graph.setEdgeWeight(edge, sc.getDistanceInMinutes());
        }
      log.info("Graph initialization complete");
    }

    public List<Station> getPathList(Station from, Station to) {
        GraphPath<Station, DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(graph, from, to);
        if (path == null) {
            return new ArrayList<>();
        }
        return path.getVertexList();
    }

    public void addEdge(Station fr, Station to, long dist) {
        DefaultWeightedEdge edge = graph.addEdge(fr, to);
        graph.setEdgeWeight(edge, dist);
        log.info("Added edge from {} to {} with distance {}", fr.getName(), to.getName(), dist);
    }

    public void updateEdge(Station fr, Station to, long dist) {
        DefaultWeightedEdge edge = graph.getEdge(fr, to);
        graph.setEdgeWeight(edge, dist);
        log.info("Updated edge from {} to {} with distance {}", fr.getName(), to.getName(), dist);
    }

    public void addVertex(Station station) {
        graph.addVertex(station);
        log.info("Added vertex {}", station.getName());
    }

    public void deleteVertex(Station station) {
        graph.removeVertex(station);
        log.info("Removed vertex {}", station.getName());
    }

    public void deleteEdge(Station fr, Station to) {
        graph.removeEdge(fr, to);
        log.info("Removed edge from {} to {}", fr.getName(), to.getName());
    }
}