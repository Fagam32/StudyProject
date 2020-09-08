package com.ivolodin.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tickets", "path"})
@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "train_name", unique = true)
    private String trainName;

    @Column(name = "total_seats_number", nullable = false)
    private int seatsNumber;

    @OneToOne
    @JoinColumn(name = "from_station", nullable = false)
    private Station fromStation;

    @OneToOne
    @JoinColumn(name = "to_station", nullable = false)
    private Station toStation;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival")
    private LocalDateTime arrival;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "train")
    private Set<Ticket> tickets;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "train")
    private List<TrainEdge> path;
}
