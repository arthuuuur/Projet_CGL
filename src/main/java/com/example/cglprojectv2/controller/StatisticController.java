package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.service.StatisticService;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/statistique")
public class StatisticController {

    private final StatisticService statisticService;

    /**
     * Constructor
     * @param statisticService the statistic service
     */
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RequestMapping("/")
    public String index(ModelMap model) {
        JSONObject nbBusinessByBusinessProvider = statisticService.getNumberOfBusinessForEachBusinessProvider();
        JSONObject amountEarnedByBusinessProvider = statisticService.getAmountEarnedByBusinessProvider();
        model.addAttribute("nbBusinessByBusinessProvider", nbBusinessByBusinessProvider);
        model.addAttribute("amountEarnedByBusinessProvider", amountEarnedByBusinessProvider);
        return "statistic/index";
    }

}
