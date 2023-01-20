package com.example.cglprojectv2.controller.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.service.business.BusinessService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/affaires")
public class BusinessController {
    private static final String ERROR_404 = "error/404";
    private static final String ERROR_500 = "error/500";
    private static final String REDIRECT_AFFAIRES = "redirect:/affaires/";

    /**
     * Business service
     */
    private final BusinessService businessService;

    /**
     * Constructor
     *
     * @param businessService the service for business entity
     */
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    /**
     * @return the view of all business
     */
    @GetMapping(value = "/")
    public String index() {
        return "business/index";
    }

    /**
     * @param id the id of the business to view
     * @return the view of the business if it exists, the view of 404 error otherwise
     */
    @GetMapping(value = "/afficher")
    public String view(@RequestParam long id, ModelMap model) {
        Optional<Business> business = businessService.get(id);
        if (business.isEmpty()) {
            return ERROR_404;
        }
        model.addAttribute("business", business.get());
        model.addAttribute("commissions", business.get().getCommissions());
        return "business/view";
    }

    /**
     * @param request the request
     * @return the view of all business
     */
    @PostMapping(value = "/creer")
    public String create(HttpServletRequest request) {
        if (businessService.createBusiness(request) == null) {
            return ERROR_500;
        }
        return REDIRECT_AFFAIRES;
    }

    /**
     * @param request the request
     * @return the view of the form to edit the business if the validation failed or 404 error view if the business to update don't exist, the view of all business otherwise
     */
    @PostMapping(value = "/modifier")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(HttpServletRequest request) {
        String id = request.getParameter("businessId");
        if (id == null || id.isEmpty()) {
            return ERROR_404;
        }
        businessService.updateBusiness(Long.parseLong(id), request);
        return REDIRECT_AFFAIRES;
    }

    /**
     * @param request the request
     * @return the view of all business
     */
    @PostMapping(value = "/supprimer")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("entityId");
        if (id == null || id.isEmpty()) {
            return ERROR_404;
        }

        businessService.delete(Long.parseLong(id));
        return REDIRECT_AFFAIRES;
    }
}





