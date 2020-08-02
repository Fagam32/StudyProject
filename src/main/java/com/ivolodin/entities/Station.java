package com.ivolodin.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode
@Entity
@Getter
@Setter
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Train> trainList;

    public Station() {
    }

    public Station(String stationName) {
        this.name = stationName;
    }

}

