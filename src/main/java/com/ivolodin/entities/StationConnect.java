package com.ivolodin.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "stationConnects")
public class StationConnect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "fromStation")
    private Station from;

    @OneToOne
    @JoinColumn(name = "toStation")
    private Station to;

    @Column
    private long distanceInMinutes;

    public StationConnect(Station from, Station to, long distance) {
        this.from = from;
        this.to = to;
        this.distanceInMinutes = distance;
    }
}
