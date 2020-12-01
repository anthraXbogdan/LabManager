package com.vaadin.tutorial.crm.ui.views.biologicalMaterialBank;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.entities.ActiveSubstance;
import com.vaadin.tutorial.crm.backend.services.activeSubstanceService.ActiveSubstanceService;
import com.vaadin.tutorial.crm.ui.MainLayout;
import org.springframework.security.access.annotation.Secured;

import java.util.HashSet;
import java.util.TreeSet;

@Route(value = "biologicalMaterialBank", layout = MainLayout.class)
@PageTitle("Biological Material Bank")
@Secured({"ADMIN", "MICROBIOLOGIST", })
public class BiologicalMaterialBankView extends VerticalLayout {

    private final ActiveSubstanceService activeSubstanceService;

    private final Grid<ActiveSubstance> grid = new Grid<>(ActiveSubstance.class);
    private final TextField filterText = new TextField();
    private final NewVialForm form;
    private final Dialog deleteVialDialog;
    private final Button addNewVialButton = new Button("New Vial");
    private final HorizontalLayout statisticsToolbar = new HorizontalLayout();
    private final Button statisticsButton = new Button("Statistics");
    private final ComboBox<String> substancesList = new ComboBox<>();
    private final Span totalVials = new Span();
    private final Span bestTitre = new Span();
    private final Span vialsStats = new Span();
    Icon statsIcon = new Icon(VaadinIcon.PILL);
    Icon newVialIcon = new Icon(VaadinIcon.FLASK);

    public BiologicalMaterialBankView(ActiveSubstanceService activeSubstanceService) {
        this.activeSubstanceService = activeSubstanceService;

        addClassName("list-view");
        setSizeFull(); // If you want your application to use all the browser view, nothing more or less,
                       // you should use setSizeFull() for the root layout.

        configureGrid();
        configureStatisticsToolbar();
        configureVialsStats();
        totalVials.addClassName("total-vials-stats");
        bestTitre.addClassName("best-titre-stats");

        form = new NewVialForm(activeSubstanceService);
        form.addListener(NewVialForm.SaveEvent.class, this:: saveNewVial);
        form.addListener(NewVialForm.CloseEvent.class, e -> closeEditor());

        deleteVialDialog = new Dialog();

        grid.setMultiSort(true);

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), statisticsToolbar, content);
        updateList();
        substancesList.setItems(updateStatistics());
        substancesList.getItemLabelGenerator();
        closeEditor();
    }

    private void saveNewVial(NewVialForm.SaveEvent event) {
        activeSubstanceService.save(event.getActiveSubstance());
        updateList();
        configureVialsStats();
        substancesList.setItems(updateStatistics());
        substancesList.getItemLabelGenerator();
        closeEditor();
    }

    private void addNewVial() {
        editNewVial(new ActiveSubstance());
    }

    private void closeEditor() {
        grid.asSingleSelect().setEnabled(true);
        form.setNewVial(null);
        form.setVisible(false);
        addNewVialButton.setEnabled(true);
        statisticsButton.setEnabled(true);
        statisticsToolbar.setVisible(false);
        removeClassName("editing");
    }

    public void editNewVial(ActiveSubstance substance) {
        if (substance == null) {
            closeEditor();
        } else {
            addNewVialButton.setEnabled(false);
            statisticsButton.setEnabled(false);
            grid.asSingleSelect().setEnabled(false);
            form.setNewVial(substance);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void deleteVial(ActiveSubstance activeSubstance) {
        deleteVialDialog.open();
        deleteVialDialog.setCloseOnOutsideClick(false);

        Button deleteVialButton = new Button("Delete vial", clickEvent -> {
            activeSubstanceService.delete(activeSubstance);
            deleteVialDialog.removeAll();
            updateList();
            configureVialsStats();
            substancesList.setItems(updateStatistics());
            substancesList.getItemLabelGenerator();
            deleteVialDialog.close();
        });

        Button cancelButton = new Button("Close", clickEvent -> {
            deleteVialDialog.removeAll();
            deleteVialDialog.close();
        });

        deleteVialButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(deleteVialButton, cancelButton);

        deleteVialDialog.add(buttons);
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
        grid.setColumns("activeSubstance", "strainName", "vialCode", "date", "colonyNo");
        grid.addColumn(ActiveSubstance::getTitre).setHeader("Titre(mg/L)").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                deleteVial(event.getValue())); // The getValue() method returns the Contact in the
                                               // selected row or null if there’s no selection.
    }

    private void configureStatisticsToolbar() {
        substancesList.setPlaceholder("Active substance");
        Button statsButton = new Button("Get statistics");
        statsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button closeStats = new Button("Close");
        closeStats.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

        expand(bestTitre);

        Icon statsIcon2 = new Icon(VaadinIcon.PILL);
        statsIcon2.setSize("30px");

        substancesList.setItems(updateStatistics());


        statisticsToolbar.add(statsIcon2, substancesList, statsButton, totalVials, bestTitre, closeStats);
        statisticsToolbar.addClassName("statistics-toolbar");
        statisticsToolbar.setAlignItems(Alignment.CENTER);
        statisticsToolbar.setJustifyContentMode(JustifyContentMode.START);
        statisticsToolbar.setSpacing(true);
        statisticsToolbar.setWidth("100%");

        closeStats.addClickListener(click -> closeEditor());
        statsButton.addClickListener(click -> showStats());
    }

    public void showStats() {
        String substance = substancesList.getValue();
        totalVials.setText("Total vials: " + activeSubstanceService.totalVialsStats(substance));
        bestTitre.setText("Best titre: " + activeSubstanceService.bestTitreStats(substance) + " mg/L");
    }

    private HorizontalLayout getToolbar() {
        filterText.setWidth("300px");
        filterText.setPlaceholder("Filter by active substance name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addNewVialButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        addNewVialButton.setIcon(newVialIcon);
        addNewVialButton.addClickListener(click -> addNewVial());

        statisticsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        statisticsButton.setIcon(statsIcon);
        statisticsButton.addClickListener(click -> openStatistics());

        H1 expansion = new H1();


        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewVialButton, statisticsButton, expansion, vialsStats);
        toolbar.addClassName("toolbar");
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setJustifyContentMode(JustifyContentMode.START);
        toolbar.setSpacing(true);
        toolbar.setWidth("100%");

        toolbar.expand(expansion);

        return toolbar;
    }

    public void openStatistics() {
        statisticsButton.setEnabled(false);
        statisticsToolbar.setVisible(true);
        addNewVialButton.setEnabled(false);
        grid.setEnabled(false);
    }

    private void updateList() {
        grid.setItems(activeSubstanceService.findAll(filterText.getValue()));
    }

    private void configureVialsStats() {
        vialsStats.setText("Total vials present in bank: " + activeSubstanceService.count());
        vialsStats.addClassName("vial-stats");
    }

    private TreeSet<String> updateStatistics() {
        TreeSet<String> activeSubstances2 = new TreeSet<>();
        HashSet<ActiveSubstance> activeSubstances = new HashSet<>(activeSubstanceService.findAll());

        for (ActiveSubstance activeSubstance : activeSubstances) {
            activeSubstances2.add(activeSubstance.getActiveSubstance());
        }
        return activeSubstances2;
    }

}
