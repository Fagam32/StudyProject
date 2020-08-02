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

    @JoinColumn(name = "trainId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Train train;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JoinColumn(name = "fromStation")
    @ManyToOne
    private Station frStation;

    @JoinColumn(name = "toStation")
    @ManyToOne
    private Station toStation;

    @Column
    private LocalDateTime departure;

    @Column
    private LocalDateTime arrival;


}
