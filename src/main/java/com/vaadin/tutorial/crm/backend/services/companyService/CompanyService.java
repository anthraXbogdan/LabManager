package com.vaadin.tutorial.crm.backend.services.companyService;

import com.vaadin.tutorial.crm.backend.entities.Company;
import com.vaadin.tutorial.crm.backend.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

}