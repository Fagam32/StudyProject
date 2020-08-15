package com.ivolodin.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "train")
    @ManyToOne(fetch = FetchType.EAGER)
    private Train train;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user;

    @JoinColumn(name = "from_station")
    @ManyToOne
    private Station frStation;

    @JoinColumn(name = "to_station")
    @ManyToOne
    private Station toStation;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival")
    private LocalDateTime arrival;


}
