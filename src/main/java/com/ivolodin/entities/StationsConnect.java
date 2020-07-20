package com.ivolodin.entities;

import javax.persistence.*;

@Entity
@Table(name = "stationsConnects")
public class StationsConnect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private int from;

    private int to;
}
