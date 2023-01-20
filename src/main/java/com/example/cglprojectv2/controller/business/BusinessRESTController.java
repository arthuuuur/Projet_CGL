package com.example.cglprojectv2.controller.business;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.datatable.Page;
import com.example.cglprojectv2.entity.datatable.PagingRequest;
import com.example.cglprojectv2.service.business.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/business")
public class BusinessRESTController {

    private final BusinessService businessService;

    /**
     * Constructor
     *
     * @param businessService the business service
     */
    public BusinessRESTController(BusinessService businessService) {
        this.businessService = businessService;
    }

    /**
     * @param id the id of the business
     * @return the business
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Business> find(@PathVariable("id") Long id) {
        Optional<Business> business = businessService.get(id);
        return business.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Fetch all business for datatable index
     *
     * @param pagingRequest the paging request
     * @return the page of business
     */
    @PostMapping(value = "/fetch_businesses", produces = "application/json")
    public ResponseEntity<Page> fetchBusinesses(@RequestBody PagingRequest pagingRequest) {
        try {
            Page page = businessService.getBusinnessesArray(pagingRequest);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Fetch all undirect businesses datatable for a specific BusinessProvider
     *
     * @param id            the id of the business provider
     * @param pagingRequest the paging request
     * @return the page of undirect business
     */
    @PostMapping(value = "/fetch_undirect_user_businesses", produces = "application/json")
    public ResponseEntity<Page> fetchUndirectUserBusinesses(@RequestParam long id, @RequestBody PagingRequest pagingRequest) {
        try {
            Page page = businessService.getUndirectUserBusinnessesArray(id, pagingRequest);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Fetch all direct businesses datatable for a specific BusinessProvider
     *
     * @param id            the id of the business provider
     * @param pagingRequest the paging request
     * @return the page of direct business
     */
    @PostMapping(value = "/fetch_direct_user_businesses", produces = "application/json")
    public ResponseEntity<Page> fetchDirectUserBusinesses(@RequestParam long id, @RequestBody PagingRequest pagingRequest) {
        try {
            Page page = businessService.getDirectUserBusinnessesArray(id, pagingRequest);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
