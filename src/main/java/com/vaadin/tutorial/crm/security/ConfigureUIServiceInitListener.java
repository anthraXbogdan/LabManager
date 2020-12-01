package com.vaadin.tutorial.crm.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.tutorial.crm.ui.views.login.LoginView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    /*
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
                event.rerouteTo(LoginView.class);
        }
    }


    private void authenticateNavigation(BeforeEnterEvent event) {
        final boolean accessGranted = SecurityUtils.isUserLoggedIn();

        if (!accessGranted) {
            if (SecurityUtils.isUserLoggedIn()) {
                event.rerouteToError(AccessDeniedException.class);
            } else {
                if (!event.getLocation().getPath().equals("register")) {
                    event.rerouteTo(LoginView.class);
                }
            }
        }
    }

     */

    private void authenticateNavigation(BeforeEnterEvent event) {
        if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            if(SecurityUtils.isUserLoggedIn()) {
                event.rerouteToError(NotFoundException.class);
            } else {
                if (!event.getLocation().getPath().equals("register")) {
                    event.rerouteTo(LoginView.class);
                }
            }
        }
    }

}
