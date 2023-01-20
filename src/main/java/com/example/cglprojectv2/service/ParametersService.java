package com.example.cglprojectv2.service;

import com.example.cglprojectv2.entity.Parameters;
import com.example.cglprojectv2.repository.ParametersRepository;
import com.example.cglprojectv2.service.common.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametersService extends AbstractService<Parameters, ParametersRepository> {

    /**
     * Constructor
     * @param repository the parameters repository
     */
    public ParametersService(ParametersRepository repository) {
        super(repository);
    }

    /**
     * @return the only one parameter in the database or a default one if there is no parameter in the database
     */
    public Parameters getParameters() {
        //Because there is only one parameters in the database we can get the first one
        // but if there is no parameters in the database we return a default parameters
        return repository.findAll().stream().findFirst().orElse(Parameters.builder()
                .affiliatedDay(30)
                .maxSuccessiveSponsor(5)
                .minimumBusinessToBeAffiliated(1)
                .percentageCommission(0.05)
                .percentageCommissionSponsor(List.of(0.05, 0.05, 0.05, 0.05, 0.05))
                .build());
    }
}
