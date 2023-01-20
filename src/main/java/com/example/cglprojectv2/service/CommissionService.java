package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import com.example.cglprojectv2.repository.CommissionRepository;
import com.example.cglprojectv2.service.common.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommissionService extends AbstractService<Commission, CommissionRepository> {

    /**
     * Constructor
     * @param repository the commission repository
     */
    public CommissionService(CommissionRepository repository) {
        super(repository);
    }

    /**
     * @param businessProvider the business provider
     * @return The list of commissions of the business provider
     */
    public List<Commission> findAllByCommissionOwner(BusinessProvider businessProvider) {
        return repository.findAllByCommissionOwner(businessProvider);
    }
}
