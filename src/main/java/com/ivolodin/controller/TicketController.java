package com.ivolodin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivolodin.model.dto.TicketDto;
import com.ivolodin.model.dto.View;
import com.ivolodin.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<Object> buyTicket(@RequestBody @Valid TicketDto ticketDto,
                                            Authentication authentication,
                                            BindingResult bindingResult) {
        HashMap<String, String> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        ticketService.buyTicket(ticketDto, authentication);

        body.put("message", "Successful operation");
        return ResponseEntity.ok().body(body);
    }

    @JsonView(View.Private.class)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping
    public List<TicketDto> getUsersTickets(Authentication authentication) {
        return ticketService.getUsersTickets(authentication);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping
    public ResponseEntity<Object> cancelTicket(@Valid @RequestBody TicketDto ticketDto,
                                               Authentication authentication,
                                               BindingResult bindingResult) {
        HashMap<String, String> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        ticketService.cancelTicket(ticketDto, authentication);

        return ResponseEntity.ok().body(body);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(View.Private.class)
    @GetMapping("{trainName}")
    public List<TicketDto> getTrainTickets(@PathVariable String trainName){
        return ticketService.getTrainTickets(trainName);
    }

}
