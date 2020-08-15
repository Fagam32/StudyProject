package com.ivolodin.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Column(name = "distance_in_minutes")
    private long distanceInMinutes;

    public StationConnect(Station from, Station to, long distance) {
        this.from = from;
        this.to = to;
        this.distanceInMinutes = distance;
    }
}
