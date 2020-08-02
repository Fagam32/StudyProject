package com.ivolodin.controller;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import com.ivolodin.service.StationService;
import com.ivolodin.service.TrainService;
import com.ivolodin.utils.Utils;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
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
    public ModelAndView findTrains(@NotNull @RequestParam String stationFrom,
                                   @NotNull @RequestParam String stationTo,
                                   @NotNull @RequestParam String date) {
        ModelAndView modelAndView = new ModelAndView("searchForm");
        LocalDate trainDate = Utils.createDateFromString(date);
        Station stFr = stationService.getStationByName(stationFrom);
        Station stTo = stationService.getStationByName(stationTo);
        List<Train> trains = null;
        if (stFr != null && stTo != null && trainDate != null && trainDate.isAfter(LocalDate.now())) {
            trains = trainService.searchTrainInDate(stFr, stTo, trainDate);
        }
        if (trains == null) trains = new ArrayList<>();

        modelAndView.addObject("trains", trains);

        return modelAndView;
    }
}
