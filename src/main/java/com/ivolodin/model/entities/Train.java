package com.ivolodin.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "train_name", unique = true)
    private String trainName;

    @Column(name = "total_seats_number", nullable = false)
    private int seatsNumber;

    @OneToOne
    @JoinColumn(name = "from_station", nullable = false)
    private Station fromStation;

    @OneToOne
    @JoinColumn(name = "to_station", nullable = false)
    private Station toStation;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival")
    private LocalDateTime arrival;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "train")
    private Set<Ticket> tickets;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "train", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<TrainEdge> path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Train train = (Train) o;
        return Objects.equals(id, train.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
