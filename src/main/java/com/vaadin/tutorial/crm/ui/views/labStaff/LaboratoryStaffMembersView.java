package com.vaadin.tutorial.crm.ui.views.labStaff;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.entities.User;
import com.vaadin.tutorial.crm.backend.services.roleService.RoleService;
import com.vaadin.tutorial.crm.backend.services.userService.UserService;
import com.vaadin.tutorial.crm.ui.MainLayout;
import org.springframework.security.access.annotation.Secured;

@Route(value = "laboratoryStaff", layout = MainLayout.class)
@PageTitle("Laboratory Staff")
@Secured("ADMIN")
public class LaboratoryStaffMembersView extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> userGrid = new Grid<>(User.class);
    private final StaffMemberForm staffMemberForm;

    public LaboratoryStaffMembersView(UserService userService, RoleService roleService) {
        this.userService = userService;

        addClassName("laboratoryStaff-view");
        setSizeFull();

        configureGrid();

        staffMemberForm = new StaffMemberForm(roleService.findAll());
        staffMemberForm.addListener(StaffMemberForm.SaveEvent.class, this::saveUser);
        staffMemberForm.addListener(StaffMemberForm.DeleteEvent.class, this::deleteUser);
        staffMemberForm.addListener(StaffMemberForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(userGrid, staffMemberForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        staffMemberForm.setUser(null);
        staffMemberForm.setVisible(false);
        removeClassName("editing");
    }

    public void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            if (!user.getUsername().equals("Admin")) {
                staffMemberForm.setUser(user);
                staffMemberForm.setVisible(true);
                addClassName("editing");
            }
        }
    }

    private void saveUser(StaffMemberForm.SaveEvent event) {
        userService.saveUser(event.getUser());
        updateList();
        closeEditor();
    }

    private void deleteUser(StaffMemberForm.DeleteEvent event) {
        userService.deleteUser(event.getUser());
        updateList();
        closeEditor();
    }

    private void addUser() {
        userGrid.asSingleSelect().clear();
        editUser(new User());
    }

    private void configureGrid() {
        userGrid.addClassName("laboratoryStaff-grid");
        userGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        userGrid.setSizeFull();
        userGrid.setColumns("firstName", "lastName", "email", "username");

        userGrid.addColumn(User::getRoleAsStrings).setHeader("Roles");

        userGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        // The getValue() method returns the Contact in the selected row or null if there’s no selection.
        userGrid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));

    }

    private HorizontalLayout getToolbar() {
        Button addStaffMemberButton = new Button("Add new staff member");
        addStaffMemberButton.addClickListener(click -> addUser());
        HorizontalLayout toolbar = new HorizontalLayout(addStaffMemberButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void updateList() {
        userGrid.setItems(userService.findAll());
    }

}
