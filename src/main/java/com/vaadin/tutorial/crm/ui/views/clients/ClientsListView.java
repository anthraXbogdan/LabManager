package com.vaadin.tutorial.crm.ui.views.clients;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.entities.Client;
import com.vaadin.tutorial.crm.backend.entities.Company;
import com.vaadin.tutorial.crm.backend.services.companyService.CompanyService;
import com.vaadin.tutorial.crm.backend.services.clientService.ClientService;
import com.vaadin.tutorial.crm.ui.MainLayout;
import org.springframework.security.access.annotation.Secured;

@Route(value = "clientsList", layout = MainLayout.class)
@PageTitle("ClientsList")
@Secured({"ADMIN"})
public class ClientsListView extends VerticalLayout {

    private final ClientService clientService;

    private final Grid<Client> grid = new Grid<>(Client.class);
    private final TextField filterText = new TextField();
    private final ClientForm form;

    public ClientsListView(ClientService clientService, CompanyService companyService) {
        this.clientService = clientService;

        addClassName("list-view");
        setSizeFull(); // If you want your application to use all the browser view, nothing more or less,
                       // you should use setSizeFull() for the root layout.

        configureGrid();

        form = new ClientForm(companyService.findAll());
        form.addListener(ClientForm.SaveEvent.class, this::saveContact);
        form.addListener(ClientForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    public void editContact(Client client) {
        if (client == null) {
            closeEditor();
        } else {
            form.setContact(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveContact(ClientForm.SaveEvent event) {
        clientService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ClientForm.DeleteEvent event) {
        clientService.delete(event.getContact());
        updateList();
        closeEditor();
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Client());
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add client");
        addContactButton.addClickListener(click -> addContact());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");

        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue())); // The getValue() method returns the Contact in the
                                                // selected row or null if there’s no selection.
    }

    private void updateList() {
        grid.setItems(clientService.findAll(filterText.getValue()));
    }

}