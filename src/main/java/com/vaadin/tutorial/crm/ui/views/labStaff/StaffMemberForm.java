package com.vaadin.tutorial.crm.ui.views.labStaff;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.backend.entities.Role;
import com.vaadin.tutorial.crm.backend.entities.User;

import java.util.List;

public class StaffMemberForm extends FormLayout {

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    PasswordField password = new PasswordField("Password");
    TextField username = new TextField("Username");
    ComboBox<Role> role = new ComboBox<>("Role");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    private User user;

    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    public StaffMemberForm(List<Role>userRoles) {
        addClassName("staffMember-form");

        binder.bindInstanceFields(this); // bindInstanceFields method matches fields in
                                                            // User and StaffMemberForm based on their names.
        role.setItems(userRoles);
        role.setItemLabelGenerator(Role::getRole);

        add(firstName, lastName, email, password, username, role, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new StaffMemberForm.DeleteEvent(this, user)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(user);
            fireEvent(new StaffMemberForm.SaveEvent(this, user));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    void setUser(User user) {
        this.user = user;
        binder.readBean(user);
    }

    /**
     * Vaadin comes with an event-handling system for components:
     */
    public static abstract class StaffMemberFormFormEvent extends ComponentEvent<StaffMemberForm> {
        private final User user;

        protected StaffMemberFormFormEvent(StaffMemberForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }

    }

    public static class SaveEvent extends StaffMemberForm.StaffMemberFormFormEvent {
        SaveEvent(StaffMemberForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends StaffMemberForm.StaffMemberFormFormEvent {
        DeleteEvent(StaffMemberForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends StaffMemberForm.StaffMemberFormFormEvent {
        CloseEvent(StaffMemberForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
