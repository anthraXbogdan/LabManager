package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.ui.views.biologicalMaterialBank.BiologicalMaterialBankView;
import com.vaadin.tutorial.crm.ui.views.dashboard.DashboardView;
import com.vaadin.tutorial.crm.ui.views.labStaff.LaboratoryStaffMembersView;
import com.vaadin.tutorial.crm.ui.views.clients.ClientsListView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport("./styles/shared-styles.css")
public class MainLayout  extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        viewTitle = new H1();
        viewTitle.addClassName("view-title");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        H1 currentUserName = new H1(currentUser);
        currentUserName.addClassName("current-username");

        Anchor logout = new Anchor("logout", "Log out");
        logout.addClassName("logout-link");
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("drawer-toggle");

        HorizontalLayout header = new HorizontalLayout(drawerToggle, viewTitle, currentUserName, logout);
        header.expand(viewTitle);
        header.addClassName("header");
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setWidth("100%");

        addToNavbar(header);
    }

    private void createDrawer() {
        H1 logo = new H1("LabMANAGER");
        logo.addClassName("app-logo");

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.addClassName("logo-layout");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        logoLayout.add(logo);

        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        dashboardLink.addClassName("dashboard-link");

        RouterLink clientsListLink = new RouterLink("Clients List", ClientsListView.class);
        clientsListLink.addClassName("list-link");

        RouterLink laboratoryStaff = new RouterLink("Laboratory Staff", LaboratoryStaffMembersView.class);
        laboratoryStaff.addClassName("laboratoryStaff-link");

        RouterLink biologicalMaterialBank = new RouterLink("Biological Material Bank", BiologicalMaterialBankView.class);
        biologicalMaterialBank.addClassName("biologicalMaterialBank-link");

        clientsListLink.setHighlightCondition(HighlightConditions.sameLocation());

        VerticalLayout verticalLayout = new VerticalLayout(dashboardLink, clientsListLink, laboratoryStaff, biologicalMaterialBank);
        verticalLayout.addClassName("vertical-drawer");
        verticalLayout.setHeightFull();
        verticalLayout.getThemeList().set("light", true);

        addToDrawer(logoLayout);
        addToDrawer(verticalLayout);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
