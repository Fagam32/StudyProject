package com.ivolodin.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "seatsNumber", nullable = false)
    private int seatsNumber;

    @OneToOne
    @JoinColumn(name = "stationFrom", nullable = false)
    private Station fromStation;

    @OneToOne
    @JoinColumn(name = "stationTo", nullable = false)
    private Station toStation;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival")
    private LocalDateTime arrival;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "train", cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "train")
    private List<TrainEdge> path;

    public Train(int seatsNumber, Station fromStation, Station toStation, LocalDateTime departure) {
        this.seatsNumber = seatsNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departure = departure;
    }

}
