package com.ivolodin.controller;

import com.ivolodin.entities.Ticket;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import com.ivolodin.service.TicketService;
import com.ivolodin.service.TrainService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ModelAndView showAllTrains() {
        ModelAndView modelAndView = new ModelAndView("trains");
        List<Train> allTrains = trainService.getAllTrains();
        modelAndView.addObject("trains", allTrains);
        return modelAndView;
    }

    @GetMapping("/{trainId}")
    public ModelAndView showTrainPathAndTickets(@PathVariable Integer trainId) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("trainPathAndTickets");
        Train train = trainService.getTrainById(trainId);
        if (train == null) {
            throw new NotFoundException("No train with ID: {" + trainId + "} found ");
        }

        List<TrainEdge> path = trainService.getTrainPath(train);
        Set<Ticket> tickets = ticketService.getTicketsOnTrain(train);
        modelAndView.addObject("train", train);
        modelAndView.addObject("path", path);
        modelAndView.addObject("tickets", tickets);
        return modelAndView;
    }

    @PostMapping(params = "trainId")
    public String deleteTrain(@RequestParam(name = "trainId") Integer trainId) {
        trainService.deleteTrainById(trainId);
        return "redirect:/trains";
    }

    @PostMapping(params = {"fromStation", "toStation", "date", "seats"})
    public String addTrain(@RequestParam(name = "fromStation") String frStat,
                           @RequestParam(name = "toStation") String toStat,
                           @RequestParam(name = "date") String departure,
                           @RequestParam(name = "seats") int seats
    ) {
        trainService.makeNewTrain(frStat, toStat, departure, seats);
        return "redirect:/trains";
    }


}
