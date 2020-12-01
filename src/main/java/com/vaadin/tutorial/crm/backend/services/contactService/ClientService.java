package com.vaadin.tutorial.crm.backend.services.contactService;

import com.vaadin.tutorial.crm.backend.entities.Client;
import com.vaadin.tutorial.crm.backend.entities.Company;
import com.vaadin.tutorial.crm.backend.repositories.CompanyRepository;
import com.vaadin.tutorial.crm.backend.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClientService {

    private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;

    private final CompanyRepository companyRepository;

    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<Client> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return clientRepository.findAll();
        } else {
            return clientRepository.search(stringFilter);
        }
    }

    public long count() {
        return clientRepository.count();
    }

    public void delete(Client client) {
        clientRepository.delete(client);
    }

    public void save(Client client) {
        if (client == null) {
            LOGGER.log(Level.SEVERE,"Contact is null. " +
                    "Are you sure you have connected your form to the application?");
            return;
        }
        clientRepository.save(client);
    }

    // method for initial population of the database.

    @PostConstruct
    public void populateTestData() {
        if (companyRepository.count() == 0) {
            companyRepository.saveAll(
                    Stream.of("Antares Biotechnology", "Icarus Bioengineering", "Nova Pharmaceuticals")
                                    .map(Company::new)
                                    .collect(Collectors.toList()));
        }

        if (clientRepository.count() == 0) {
            Random r = new Random(0);
            List<Company> companies = companyRepository.findAll();
            clientRepository.saveAll(
                    Stream.of("Gabrielle Patel", "Brian Robinson", "Eduardo Haugen", "Koen Johansen",
                            "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                            "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                            "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                            "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson ", "Lara Martin",
                            "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                            "Jaydan Jackson", "Bernard Nilsen")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Client client = new Client();
                                client.setFirstName(split[0]);
                                client.setLastName(split[1]);
                                client.setCompany(companies.get(r.nextInt(companies.size())));
                                client.setStatus(Client.Status.values()[r.nextInt(Client
                                        .Status.values().length)]);
                                String email = (client.getFirstName() + "." + client
                                        .getLastName() + "@" + client.getCompany().getName().replaceAll("[\\s-]", "") +
                                        ".com").toLowerCase();
                                client.setEmail(email);
                                return client;
                            }).collect(Collectors.toList()));
        }

    }

}