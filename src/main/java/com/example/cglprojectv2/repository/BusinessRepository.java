package com.example.cglprojectv2.repository;

import com.example.cglprojectv2.entity.Business;
import com.example.cglprojectv2.entity.BusinessProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long > {

    int countByBusinessProvider(BusinessProvider businessProvider);
}