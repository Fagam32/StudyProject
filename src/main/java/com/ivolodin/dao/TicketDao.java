package com.ivolodin.dao;


import com.ivolodin.entities.Ticket;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.User;

import java.util.List;
import java.util.Set;

public interface TicketDao {

    Ticket getById(int id);

    void update(Ticket ticket);

    void delete(Ticket ticket);

    void add(Ticket ticket);

    List<Ticket> getAll();

    Set<Ticket> getAllForTrain(Train train);

    List<Ticket> getTicketsFromUser(User user);
}
