package com.as2flow.application.views.drawer.partners;

import com.as2flow.application.backend.entity.Partner;
import com.as2flow.application.backend.service.PartnerService;
import com.as2flow.application.views.main.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "partners", layout = MainView.class)
@PageTitle("Partners")
public class PartnersView extends HorizontalLayout
{
    private final PartnerService partnerService;
    private final Grid<Partner> grid = new Grid<>(Partner.class);

    public PartnersView(PartnerService partnerService)
    {
        this.partnerService = partnerService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(grid);
        updateList();
    }

    private void updateList()
    {
        grid.setItems(partnerService.findAll());
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
