package com.ivolodin.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrainEdge trainEdge = (TrainEdge) o;
        return Objects.equals(id, trainEdge.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
