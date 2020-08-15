package com.ivolodin.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Getter
@Setter
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    public Station(String stationName) {
        this.name = stationName;
    }

}


