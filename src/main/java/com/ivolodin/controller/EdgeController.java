package com.ivolodin.controller;

import com.ivolodin.entities.StationConnect;
import com.ivolodin.model.EdgeForm;
import com.ivolodin.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/edges")
public class EdgeController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public ModelAndView getAllEdges() {
        ModelAndView modelAndView = new ModelAndView("edges");
        List<StationConnect> allEdges = stationService.getAllEdges();
        modelAndView.addObject("edges", allEdges);
        return modelAndView;
    }

    @PostMapping(params = "edgeId")
    public String deleteEdge(@RequestParam Integer edgeId) {
        stationService.deleteEdge(edgeId);
        return "redirect:/edges";
    }

    @PostMapping(params = {"fromStation", "toStation", "distance"})
    public String addEdge(@Valid EdgeForm edgeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "redirect:/edges";
        stationService.addEdge(edgeForm);
        return "redirect:/edges";
    }
}
