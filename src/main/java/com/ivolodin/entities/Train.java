package com.ivolodin.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "seatsNumber", nullable = false)
    private int seatsNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stationFrom", nullable = false)
    private Station fromStation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stationTo", nullable = false)
    private Station toStation;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival", nullable = false)
    private LocalDateTime arrival;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Train() {
    }

    public Train(int seatsNumber, Station fromStation, Station toStation, LocalDateTime departure, LocalDateTime arrival) {
        this.seatsNumber = seatsNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departure = departure;
        this.arrival = arrival;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station from) {
        this.fromStation = from;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station to) {
        this.toStation = to;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", seatsNumber=" + seatsNumber +
                ", from=" + fromStation +
                ", to=" + toStation +
                ", departure=" + departure +
                ", arrival=" + arrival +
                '}';
    }
}
