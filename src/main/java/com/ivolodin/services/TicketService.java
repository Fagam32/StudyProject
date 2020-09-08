package com.ivolodin.services;

import com.ivolodin.dto.TicketDto;
import com.ivolodin.entities.*;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TicketRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.repositories.UserRepository;
import com.ivolodin.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class TicketService {

    @Autowired
    private TrainService trainService;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private StationRepository stationRepository;


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    public void buyTicket(TicketDto ticketDto, Authentication authentication) {
        Train train = trainRepository.findTrainByTrainName(ticketDto.getTrainName());
        Station frSt = stationRepository.findByName(ticketDto.getFromStation());
        Station toSt = stationRepository.findByName(ticketDto.getToStation());

        if (train == null)
            throw new EntityNotFoundException("Train with name:{" + ticketDto.getTrainName() + "} not found");
        if (frSt == null)
            throw new EntityNotFoundException("Station with name:{" + ticketDto.getFromStation() + "} not found");
        if (toSt == null)
            throw new EntityNotFoundException("Station with name:{" + ticketDto.getToStation() + "} not found");

        User user = userService.getUserFromAuthentication(authentication);

        TrainEdge fromEdge = getDepartingStation(train.getPath(), ticketDto.getFromStation());
        TrainEdge toEdge = getArrivalStation(train.getPath(), ticketDto.getToStation());
        if (fromEdge == null || toEdge == null)
            throw new IllegalArgumentException("Something went wrong");

        if (fromEdge.getDeparture().minusMinutes(10).isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Ticket can't be bought for train departing in 10 or less minutes");

        checkUserTickets(train, user);

        if (!trainService.hasAvailableSeatsOnPath(train, fromEdge, toEdge))
            throw new IllegalArgumentException("No available seats");
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);

        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setFromEdge(fromEdge);
        ticket.setToEdge(toEdge);

        ticketRepository.save(ticket);

        trainService.updateSeatsForTrain(train, fromEdge, toEdge, -1);

    }

    private void checkUserTickets(Train train, User user) {
        Set<Ticket> tickets = train.getTickets();
        for (Ticket trainTicket : tickets) {
            if (trainTicket.getUser().equals(user))
                throw new IllegalArgumentException("This user already bought ticket");
        }
    }

    private TrainEdge getArrivalStation(List<TrainEdge> path, String stationName) {
        //looking from end
        for (int i = path.size() - 1; i >= 0; i--) {
            TrainEdge edge = path.get(i);
            if (edge.getStation().getName().equals(stationName))
                return edge;
        }
        return null;
    }

    private TrainEdge getDepartingStation(List<TrainEdge> path, String stationName) {
        for (TrainEdge edge : path) {
            if (edge.getStation().getName().equals(stationName))
                return edge;
        }
        return null;
    }

    public List<TicketDto> getUsersTickets(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        return MapperUtils.mapAll(user.getTickets(), TicketDto.class);
    }


    public void cancelTicket(TicketDto ticketDto, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        for (Ticket ticket : user.getTickets()) {
            String frName = ticket.getFromEdge().getStation().getName();
            String toName = ticket.getToEdge().getStation().getName();
            String trainName = ticket.getTrain().getTrainName();
            if (frName.equals(ticketDto.getFromStation())
                    && toName.equals(ticketDto.getToStation())
                    && trainName.equals(ticketDto.getTrainName())) {
                ticketRepository.delete(ticket);
                break;
            }
        }
    }

    public List<TicketDto> getTrainTickets(String trainName) {
        Train train = trainRepository.findTrainByTrainName(trainName);
        if (train == null)
            return new ArrayList<>();
        return MapperUtils.mapAll(train.getTickets(), TicketDto.class);
    }
}
