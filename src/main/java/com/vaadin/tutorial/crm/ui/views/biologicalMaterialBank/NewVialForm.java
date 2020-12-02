package com.vaadin.tutorial.crm.ui.views.biologicalMaterialBank;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.backend.entities.ActiveSubstance;
import com.vaadin.tutorial.crm.backend.services.activeSubstanceService.ActiveSubstanceService;

import javax.validation.constraints.NotEmpty;

public class NewVialForm extends FormLayout {

    private final Button save = new Button("Add Vial");
    private final Button cancel = new Button("Cancel");
    private final Icon newVialIcon = new Icon(VaadinIcon.FLASK);

    private ActiveSubstance substance;
    private final ActiveSubstanceService activeSubstanceService;

    BeanValidationBinder<ActiveSubstance> binder = new BeanValidationBinder<>(ActiveSubstance.class);

    public NewVialForm(ActiveSubstanceService activeSubstanceService) {
        this.activeSubstanceService = activeSubstanceService;
        addClassName("contact-form");

        binder.bindInstanceFields(this); // bindInstanceFields method matches fields in
                                                            // Contact and ContactForm based on their names.

        TextField activeSubstance = new TextField("Active Substance");
        TextField strainName = new TextField("Strain Name");
        TextField vialCode = new TextField("Vial Code");
        @NotEmpty
        DatePicker date = new DatePicker("Lyophilisation date");
        TextField colonyNo = new TextField("Colony Number");
        TextField titre = new TextField("Titre");

        add(activeSubstance, strainName, vialCode, date, colonyNo, titre, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        save.addClickShortcut(Key.ENTER);
        save.setIcon(newVialIcon);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> {
            try {
                validateAndSave();
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });

        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, cancel);
    }

    private void validateAndSave() throws ValidationException {
        binder.writeBean(substance);

        if (activeSubstanceService.codeValidator(substance)) {
            fireEvent(new SaveEvent(this, substance));
        } else {
            Div vialCodeNotification = new Div();
            vialCodeNotification.addClassName("vialCode-notification");
            vialCodeNotification.setText("The Vial Code must be unique!");
            Notification notification = new Notification(vialCodeNotification);
            notification.setDuration(3000);
            notification.addThemeVariants();
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }

    }

    void setNewVial(ActiveSubstance substance) {
        this.substance = substance;
        binder.readBean(substance);
    }

    /**
     * Vaadin comes with an event-handling system for components:
     */
    public static abstract class NewVialFormEvent extends ComponentEvent<NewVialForm> {
        private final ActiveSubstance substance;

        protected NewVialFormEvent(NewVialForm source, ActiveSubstance substance) {
            super(source, false);
            this.substance = substance;
        }

        public ActiveSubstance getActiveSubstance() {
            return substance;
        }

    }

    public static class SaveEvent extends NewVialForm.NewVialFormEvent {
        SaveEvent(NewVialForm source, ActiveSubstance substance) {
            super(source, substance);
        }
    }

    public static class DeleteEvent extends NewVialForm.NewVialFormEvent {
        DeleteEvent(NewVialForm source, ActiveSubstance substance) {
            super(source, substance);
        }
    }

    public static class CloseEvent extends NewVialForm.NewVialFormEvent {
        CloseEvent(NewVialForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
