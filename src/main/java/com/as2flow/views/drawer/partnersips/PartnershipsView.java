package com.as2flow.views.drawer.partnersips;

import com.as2flow.backend.entity.Partnership;
import com.as2flow.backend.service.PartnershipService;
import com.as2flow.views.MainView;
import com.as2flow.views.PartnershipForm;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.commons.collection.attr.IStringMap;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "partnerships", layout = MainView.class)
@PageTitle("Partnerships")
@CssImport("./views/drawer/partnerships-view.css")
public class PartnershipsView extends VerticalLayout
{
    private final PartnershipService partnershipService;
    private final Grid<Partnership> grid = new Grid<>(Partnership.class);
    private final TextField filterText = new TextField();
    private final PartnershipForm partnershipForm;

    public PartnershipsView(PartnershipService partnershipService)
    {
        this.partnershipService = partnershipService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        partnershipForm = new PartnershipForm();
        partnershipForm.addListener(PartnershipForm.SaveEvent.class, this::savePartnership);
        partnershipForm.addListener(PartnershipForm.DeleteEvent.class, this::deletePartnership);
        partnershipForm.addListener(PartnershipForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid, partnershipForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add partnership");
        addContactButton.addClickListener(click -> addPartnership());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addPartnership() {
        grid.asSingleSelect().clear();
        editPartnership(new Partnership());
    }

    private void savePartnership(PartnershipForm.SaveEvent event) {
        partnershipService.save(event.getPartnership());
        updateList();
        closeEditor();
    }

    private void deletePartnership(PartnershipForm.DeleteEvent event) {
        partnershipService.delete(event.getPartnership());
        updateList();
        closeEditor();
    }

    private void configureFilter()
    {
        filterText.setPlaceholder("Filter...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList()
    {
        grid.setItems(partnershipService.findAll(filterText.getValue()));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Partnership::getName).setHeader("Name");
        grid.addColumn(partnership -> {
            IStringMap m = partnership.getAttributes();
            return m.getValue(CPartnershipIDs.PA_SUBJECT);
        }).setHeader("Subject");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editPartnership(event.getValue()));
    }

    public void editPartnership(Partnership partnership) {
        if (partnership == null) {
            closeEditor();
        } else {
            partnershipForm.setPartnership(partnership);
            partnershipForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        partnershipForm.setPartnership(null);
        partnershipForm.setVisible(false);
        removeClassName("editing");
    }
}
