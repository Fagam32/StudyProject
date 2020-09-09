package com.ivolodin.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"train", "station", "seatsLeft"})
@Entity
@Table(name = "train_paths")
public class TrainEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "train")
    @ManyToOne
    private Train train;

    @JoinColumn(name = "station")
    @OneToOne
    private Station station;

    @Column(name = "seats_left")
    private int seatsLeft;

    @Column(name = "arrival")
    private LocalDateTime arrival;

    @Column(name = "standing")
    private int standingMinutes;

    @Column(name = "departure")
    private LocalDateTime departure;


    @Column(name = "order_in_path")
    private Integer order;
}
