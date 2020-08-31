package com.ivolodin.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "station_connects")
public class StationConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "from_station")
    private Station from;

    @OneToOne
    @JoinColumn(name = "to_station")
    private Station to;

    @Column
    private Integer distanceInMinutes;

}
