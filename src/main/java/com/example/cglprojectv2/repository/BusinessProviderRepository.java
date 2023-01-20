package com.example.cglprojectv2.repository;

import com.example.cglprojectv2.entity.BusinessProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessProviderRepository extends JpaRepository<BusinessProvider, Long > {
    BusinessProvider findByUserName(String userName);
}
