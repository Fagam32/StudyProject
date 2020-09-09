package com.ivolodin.services;

import com.ivolodin.dto.TicketDto;
import com.ivolodin.entities.*;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TicketRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Transactional
@Service
public class TicketService {

    private TrainService trainService;

    private TrainRepository trainRepository;

    private StationRepository stationRepository;

    private TicketRepository ticketRepository;

    private UserService userService;

    /**
     * @param ticketDto      valid ticketDto with train name, from station and to station
     * @param authentication authentication containing information about user authentication
     * @throws EntityNotFoundException if any of train or stations doesn't not exists
     */
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
        log.info("Ticket {} is bought", ticket);
    }

    /**
     * Checks if user has already bought ticket for given train
     *
     * @param train train to check
     * @param user  user to check
     * @throws IllegalArgumentException if such ticket is found
     */
    private void checkUserTickets(Train train, User user) {
        Set<Ticket> tickets = train.getTickets();
        for (Ticket trainTicket : tickets) {
            if (trainTicket.getUser().equals(user))
                throw new IllegalArgumentException("This user already bought ticket");
        }
    }

    /**
     * Looks for TrainEdge in given TrainPath, starting from end
     *
     * @param path        list of TrainEdge from train
     * @param stationName station to find
     * @return found TrainEdge or null
     */
    private TrainEdge getArrivalStation(List<TrainEdge> path, String stationName) {
        //looking from end
        for (int i = path.size() - 1; i >= 0; i--) {
            TrainEdge edge = path.get(i);
            if (edge.getStation().getName().equals(stationName))
                return edge;
        }
        return null;
    }

    /**
     * Looks for TrainEdge in given TrainPath, starting from beginning
     *
     * @param path        list of TrainEdge from train
     * @param stationName station to find
     * @return found TrainEdge or null
     */
    private TrainEdge getDepartingStation(List<TrainEdge> path, String stationName) {
        for (TrainEdge edge : path) {
            if (edge.getStation().getName().equals(stationName))
                return edge;
        }
        return null;
    }

    /**
     * Returns user's tickets from authentication
     *
     * @param authentication authentication containing information about user authentication
     * @return list of TicketDto
     */
    public List<TicketDto> getUsersTickets(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        return MapperUtils.mapAll(user.getTickets(), TicketDto.class);
    }


    /**
     * Deletes ticket from database and updates available tickets train paths
     *
     * @param ticketDto      ticket to delete
     * @param authentication authentication containing information about user authentication
     */
    public void cancelTicket(TicketDto ticketDto, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        for (Ticket ticket : user.getTickets()) {
            String frName = ticket.getFromEdge().getStation().getName();
            String toName = ticket.getToEdge().getStation().getName();
            String trainName = ticket.getTrain().getTrainName();
            if (frName.equals(ticketDto.getFromStation())
                    && toName.equals(ticketDto.getToStation())
                    && trainName.equals(ticketDto.getTrainName())) {
                log.info("Ticket {} is canceled", ticket);
                ticketRepository.delete(ticket);
                break;
            }
        }
        Train train = trainRepository.findTrainByTrainName(ticketDto.getTrainName());

        trainService.updateSeatsForTrain(train, ticketDto.getFromStation(), ticketDto.getToStation(), 1);
    }

    /**
     * Returns all tickets for given train, if such train exists
     *
     * @param trainName name of train
     * @return list of ticketDto
     */
    public List<TicketDto> getTrainTickets(String trainName) {
        Train train = trainRepository.findTrainByTrainName(trainName);
        if (train == null)
            return new ArrayList<>();
        return MapperUtils.mapAll(train.getTickets(), TicketDto.class);
    }

    @Autowired
    public void setTrainService(TrainService trainService) {
        this.trainService = trainService;
    }

    @Autowired
    public void setTrainRepository(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Autowired
    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
