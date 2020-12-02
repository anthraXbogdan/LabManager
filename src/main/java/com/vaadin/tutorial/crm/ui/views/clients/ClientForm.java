package com.vaadin.tutorial.crm.ui.views.clients;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.backend.entities.Client;
import com.vaadin.tutorial.crm.backend.entities.Company;

import java.util.List;

public class ClientForm extends FormLayout {

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");

    private Client client;

    /**
     * BeanValidationBinder is a Binder that is aware of bean validation annotations. By
     * passing it in the Contact.class, we define the type of object we are binding to.
     */
    BeanValidationBinder<Client> binder = new BeanValidationBinder<>(Client.class);

    public ClientForm(List<Company> companies) {
        addClassName("contact-form");

        binder.bindInstanceFields(this); // bindInstanceFields method matches fields in
                                                            // Contact and ContactForm based on their names.

        ComboBox<Company> company = new ComboBox<>("Company");
        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        ComboBox<Client.Status> status = new ComboBox<>("Status");
        status.setItems(Client.Status.values());

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField email = new EmailField("Email");

        add(firstName, lastName, email, status, company, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    void setContact(Client client) {
        this.client = client;
        binder.readBean(client);
    }


    /**
     * Vaadin comes with an event-handling system for components:
     */
    public static abstract class ContactFormEvent extends ComponentEvent<ClientForm> {
        private final Client client;

        protected ContactFormEvent(ClientForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getContact() {
            return client;
        }

    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
