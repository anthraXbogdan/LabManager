package com.vaadin.tutorial.crm.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class ActiveSubstance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    private String activeSubstance;

    @NotNull
    @NotEmpty
    private String strainName;

    @NotNull
    @NotEmpty
    private String vialCode;

    @NotNull
    private LocalDate date;

    @NotNull
    @NotEmpty
    private String colonyNo;

    @NotNull
    @NotEmpty
    private String titre;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActiveSubstance() {
        return activeSubstance;
    }

    public void setActiveSubstance(String activeSubstance) {
        this.activeSubstance = activeSubstance;
    }

    public String getStrainName() {
        return strainName;
    }

    public void setStrainName(String strainName) {
        this.strainName = strainName;
    }

    public String getVialCode() {
        return vialCode;
    }

    public void setVialCode(String vialCode) {
        this.vialCode = vialCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getColonyNo() {
        return colonyNo;
    }

    public void setColonyNo(String colonyNo) {
        this.colonyNo = colonyNo;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
