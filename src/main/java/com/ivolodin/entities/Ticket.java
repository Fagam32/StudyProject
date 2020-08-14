package com.ivolodin.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "train_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Train train;

    @JoinColumn(name = "user_id")
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

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;
}
