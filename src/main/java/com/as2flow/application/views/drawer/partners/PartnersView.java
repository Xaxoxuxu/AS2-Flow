package com.as2flow.application.views.drawer.partners;

import com.as2flow.application.backend.entity.Partner;
import com.as2flow.application.backend.service.PartnerService;
import com.as2flow.application.views.main.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "partners", layout = MainView.class)
@PageTitle("Partners")
public class PartnersView extends VerticalLayout
{
    private final PartnerService partnerService;
    private final Grid<Partner> grid = new Grid<>(Partner.class);
    private final TextField filterText = new TextField();

    public PartnersView(PartnerService partnerService)
    {
        this.partnerService = partnerService;
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
        grid.setItems(partnerService.findAll(filterText.getValue()));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.setColumns("name", "as2PartnerId", "url");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
