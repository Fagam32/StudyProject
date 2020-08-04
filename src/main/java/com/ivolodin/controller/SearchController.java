package com.ivolodin.controller;

import com.ivolodin.entities.Train;
import com.ivolodin.model.SearchForm;
import com.ivolodin.service.StationService;
import com.ivolodin.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private StationService stationService;

    @Autowired
    private TrainService trainService;

    @GetMapping
    public ModelAndView sendSearchForm() {
        return new ModelAndView("searchForm");
    }

    @PostMapping
    public ModelAndView findTrains(@Valid SearchForm searchForm,
                                   BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("searchForm");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message", "Enter correct data");
            return modelAndView;
        }

        List<Train> trains = trainService.searchTrainInDate(searchForm);

        if (trains == null)
            trains = new ArrayList<>();

        modelAndView.addObject("trains", trains);
        return modelAndView;
    }
}
