package com.ivolodin.controller;

import com.ivolodin.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainController {
    private final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/addTrain")
    public ModelAndView getAllTrains() {
        ModelAndView modelAndView = new ModelAndView("addTrain");
        modelAndView.addObject("trainList", trainService.getAllTrains());
        return modelAndView;
    }

    @PostMapping("/addTrain")
    public ModelAndView addNewTrain(@RequestParam(name = "fromStation") String frStat,
                                    @RequestParam(name = "toStation") String toStat,
                                    @RequestParam(name = "date") String departure,
                                    @RequestParam(name = "seats") int seats
    ) {
        ModelAndView modelAndView = new ModelAndView("addTrain");
        trainService.makeNewTrain(frStat, toStat, departure, seats);

        modelAndView.addObject("trainList", trainService.getAllTrains());
        return modelAndView;
    }
}
