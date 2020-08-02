package com.ivolodin.controller;

import com.ivolodin.entities.Ticket;
import com.ivolodin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/buyTicket")
    public ModelAndView buyTicket(Principal principal,
                                  @RequestParam Integer trainId,
                                  @RequestParam String fromStation,
                                  @RequestParam String toStation) {
        ModelAndView modelAndView = new ModelAndView("buyTicket");

        String message = ticketService.buyTicket(principal.getName(), trainId, fromStation, toStation);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/myTickets")
    public ModelAndView getUserTickets(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("myTickets");

        List<Ticket> tickets = ticketService.getUserTickets(principal.getName());
        if (tickets == null) tickets = new ArrayList<>();
        modelAndView.addObject("tickets", tickets);

        return modelAndView;
    }
}
