package com.as2flow.application.views.drawer.partnersips;

import com.as2flow.application.backend.entity.Partnership;
import com.as2flow.application.backend.service.PartnershipService;
import com.as2flow.application.views.main.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "partnerships", layout = MainView.class)
@PageTitle("Partnerships")
public class PartnershipsView extends VerticalLayout
{
    private final PartnershipService partnershipService;
    private final Grid<Partnership> grid = new Grid<>(Partnership.class);
    private final TextField filterText = new TextField();

    public PartnershipsView(PartnershipService partnershipService)
    {
        this.partnershipService = partnershipService;
        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();

        add(filterText, grid);
        updateList();
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
        grid.setColumns("name");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
