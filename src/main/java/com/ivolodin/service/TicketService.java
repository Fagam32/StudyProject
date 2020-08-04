package com.ivolodin.service;

import com.ivolodin.dao.StationDao;
import com.ivolodin.dao.TicketDao;
import com.ivolodin.dao.TrainDao;
import com.ivolodin.dao.UserDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.Ticket;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TrainDao trainDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private TrainService trainService;

    public String buyTicket(String userName, Integer trainId, String frStation, String toStation) {
        User user = userDao.getByUsername(userName);
        Train train = trainDao.getById(trainId);
        Station frSt = stationDao.getByName(frStation);
        Station toSt = stationDao.getByName(toStation);

        return buyTicket(user, train, frSt, toSt);
    }

    public String buyTicket(User user, Train train, Station frSt, Station toSt) {
        if (user == null || train == null || frSt == null || toSt == null)
            return "Something is wrong. Try again";

        Set<Ticket> tickets = ticketDao.getAllForTrain(train);

        for (Ticket ticket : tickets) {
            if (ticket.getUser().equals(user))
                return "You have already bought ticket for this train";
        }

        LocalDateTime departure = trainService.getDepartureTimeOnStation(train, frSt);
        LocalDateTime arrival = trainService.getArrivalTimeOnStation(train, toSt);

        if (departure == null || arrival == null) {
            return "Something is wrong. Try again";
        }

        trainService.updateSeatsOnPath(train, frSt, toSt, -1);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setFrStation(frSt);
        ticket.setToStation(toSt);
        ticket.setDeparture(departure);
        ticket.setArrival(arrival);
        ticketDao.add(ticket);

        return "Order is complete!";
    }

    public List<Ticket> getUserTickets(String userName) {
        User user = userDao.getByUsername(userName);
        return ticketDao.getTicketsFromUser(user);
    }

    public void cancelTicket(Integer ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
        trainService.updateSeatsOnPath(ticket.getTrain(), ticket.getFrStation(), ticket.getToStation(), 1);
        ticketDao.delete(ticket);
    }
}
