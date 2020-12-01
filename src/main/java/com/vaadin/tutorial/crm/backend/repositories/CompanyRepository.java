package com.vaadin.tutorial.crm.backend.repositories;

import com.vaadin.tutorial.crm.backend.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}