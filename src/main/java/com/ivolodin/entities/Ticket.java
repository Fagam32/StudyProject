package com.ivolodin.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "train_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Train train;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "from_edge")
    @OneToOne
    private TrainEdge fromEdge;

    @JoinColumn(name = "to_edge")
    @OneToOne
    private TrainEdge toEdge;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;
}
