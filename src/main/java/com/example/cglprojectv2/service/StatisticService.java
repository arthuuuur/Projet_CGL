package com.example.cglprojectv2.service;

import com.example.cglprojectv2.service.business.BusinessService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    private final BusinessProviderService businessProviderService;
    private final BusinessService businessService;

    /**
     * Constructor
     *
     * @param businessProviderService businessProviderService
     * @param businessService         businessService
     */
    public StatisticService(BusinessProviderService businessProviderService, BusinessService businessService) {
        this.businessProviderService = businessProviderService;
        this.businessService = businessService;
    }

    /**
     * @return return a json object with all the business providers under the "labels" key
     * and the number of businesses for each businessProvider under the "values" key
     */
    public JSONObject getNumberOfBusinessForEachBusinessProvider() {
        JSONObject data = new JSONObject();
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        businessProviderService.getAll().forEach(businessProvider -> {
            labels.add(businessProvider.getFullName());
            values.add(businessService.countByBusinessProvider(businessProvider));

        });
        data.put("labels", labels);
        data.put("values", values);
        return data;
    }

    /**
     * @return return a json object with all the business providers under the "labels" key
     * and the total of money earned for each businessProvider under the "values" key
     */
    public JSONObject getAmountEarnedByBusinessProvider() {
        JSONObject data = new JSONObject();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        businessProviderService.getAll().forEach(businessProvider -> {
            labels.add(businessProvider.getFullName());
            values.add(businessService.getAmountEarnedByBusinessProvider(businessProvider));
        });
        data.put("labels", labels);
        data.put("values", values);
        return data;
    }
}
