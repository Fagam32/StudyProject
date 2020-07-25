package com.ivolodin.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private LocalDate birth;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "passenger")
    private List<Ticket> tickets;

}
