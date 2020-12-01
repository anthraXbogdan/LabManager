package com.vaadin.tutorial.crm.backend.services.activeSubstanceService;

import com.vaadin.tutorial.crm.backend.entities.ActiveSubstance;
import com.vaadin.tutorial.crm.backend.repositories.ActiveSubstanceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Service
public class ActiveSubstanceService {

    private final ActiveSubstanceRepository activeSubstanceRepository;

    public ActiveSubstanceService(ActiveSubstanceRepository activeSubstanceRepository) {
        this.activeSubstanceRepository = activeSubstanceRepository;
    }

    public List<ActiveSubstance> findAll() {
        return activeSubstanceRepository.findAll();
    }

    public List<ActiveSubstance> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return activeSubstanceRepository.findAll();
        } else {
            return activeSubstanceRepository.search(stringFilter);
        }
    }

    public void save(ActiveSubstance substance) {
        activeSubstanceRepository.save(substance);
    }

    public void delete(ActiveSubstance substance) {
        activeSubstanceRepository.delete(substance);
    }

    public long count() {
        return activeSubstanceRepository.count();
    }

    public boolean codeValidator(ActiveSubstance subst) {
        ArrayList<String> vialCodesList = new ArrayList<>();
        boolean flag = true;

        for (ActiveSubstance substance : activeSubstanceRepository.findAll()) {
            vialCodesList.add(substance.getVialCode());
        }

        String currentCode = subst.getVialCode();

        for (String comparingCode : vialCodesList) {
            if (comparingCode.equals(currentCode)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public long totalVialsStats(String selectedSubstance) {
        int count = 0;

        for (ActiveSubstance activeSubstance : activeSubstanceRepository.findAll()) {
            if (activeSubstance.getActiveSubstance().equals(selectedSubstance)) {
                count++;
            }
        }
        return count;
    }

    public String bestTitreStats(String selectedSubstance) {
        TreeSet<String> titresList = new TreeSet<>();

        for (ActiveSubstance activeSubstance : activeSubstanceRepository.findAll()) {
            if (activeSubstance.getActiveSubstance().equals(selectedSubstance)) {
                titresList.add(activeSubstance.getTitre());
            }
        }
        return titresList.last();
    }

}
