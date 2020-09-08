package com.ivolodin.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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
