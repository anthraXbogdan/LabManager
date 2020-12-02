package com.vaadin.tutorial.crm.ui.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.services.activeSubstanceService.ActiveSubstanceService;
import com.vaadin.tutorial.crm.backend.services.companyService.CompanyService;
import com.vaadin.tutorial.crm.backend.services.clientService.ClientService;
import com.vaadin.tutorial.crm.ui.MainLayout;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    private final ClientService clientService;
    private final ActiveSubstanceService activeSubstanceService;


    public DashboardView(ClientService clientService, CompanyService companyService, ActiveSubstanceService activeSubstanceService) {
        this.clientService = clientService;
        this.activeSubstanceService = activeSubstanceService;
        addClassName("dashboard-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout bioreactorImage = new HorizontalLayout();
        bioreactorImage.setWidth("1020px");
        bioreactorImage.setHeight("570px");
        bioreactorImage.getElement().getStyle().set("background-image" , "url('bioreactor.jpg')" );

        add(bioreactorImage);
    }

    // methods for future utilization
    private Component getContactStats() {
        Span stats = new Span(clientService.count() + " contacts");
        stats.addClassName("contact-stats");
        return stats;
    }

    private Component getVialsStats() {
        Span stats = new Span(activeSubstanceService.count() + " biological material vials");
        stats.addClassName("contact-stats");
        return stats;
    }

}
