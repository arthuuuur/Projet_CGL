package com.example.cglprojectv2.repository;

import com.example.cglprojectv2.entity.BusinessProvider;
import com.example.cglprojectv2.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long > {
        List<Commission> findAllByCommissionOwner(BusinessProvider businessProvider);
}
