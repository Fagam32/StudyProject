package com.ivolodin.controller;

import com.ivolodin.entities.Train;
import com.ivolodin.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping
    public ModelAndView showAllTrains() {
        ModelAndView modelAndView = new ModelAndView("trains");
        List<Train> allTrains = trainService.getAllTrains();
        modelAndView.addObject("trains", allTrains);
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
