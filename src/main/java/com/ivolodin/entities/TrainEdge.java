package com.ivolodin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainPaths")
public class TrainEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "train")
    @OneToOne
    private Train train;

    @JoinColumn(name = "edge")
    @OneToOne
    private StationConnect stationConnect;

    @Column(name = "seatsLeft")
    private int seatsLeft;

    @Column
    private LocalDateTime arrival;

    public TrainEdge(Train train, StationConnect stationConnect, int seatsLeft){
        this.train = train;
        this.stationConnect = stationConnect;
        this.seatsLeft = seatsLeft;
    }
}
