package com.example.cglprojectv2.controller;

import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.service.ParametersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/parametres")
public class ParameterController {

    private final ParametersService parametersService;

    /**
     * Constructor
     *
     * @param parametersService the parameters service
     */
    public ParameterController(ParametersService parametersService) {
        this.parametersService = parametersService;
    }

    @GetMapping(value = "/")
    public String showUpdatePage(ModelMap model) {
        Parameters parameters = parametersService.getParameters();
        model.put("parameters", parameters);
        return "parameters/form";
    }

    @PostMapping(value = "/")
    public String update(@RequestParam("percentageCommissionSponsor") String[] percentageCommissionSponsor, @Valid Parameters parameters, BindingResult result) {
        if (result.hasErrors()) {
            return "parameters/form";
        }
        parameters.setPercentageCommissionSponsor(Arrays.stream(percentageCommissionSponsor).map(Double::parseDouble).toList());
        parametersService.save(parameters);
        return "redirect:/";
    }
}
