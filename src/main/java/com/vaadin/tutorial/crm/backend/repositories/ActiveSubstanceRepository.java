package com.vaadin.tutorial.crm.backend.repositories;

import com.vaadin.tutorial.crm.backend.entities.ActiveSubstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveSubstanceRepository extends JpaRepository<ActiveSubstance, Long> {

    //method for filtering rows on the grid
    @Query
    ("select c from ActiveSubstance c where lower(c.activeSubstance) like lower(concat('%', :searchTerm, '%')) " +
    "or lower(c.strainName) like lower(concat('%', :searchTerm, '%'))")
    List<ActiveSubstance> search(@Param("searchTerm") String searchTerm);

}
