package com.vaadin.tutorial.crm.backend.repositories;

import com.vaadin.tutorial.crm.backend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //method for filtering rows on the grid
    @Query
    ("select c from Client c where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
     "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Client> search(@Param("searchTerm") String searchTerm);

}