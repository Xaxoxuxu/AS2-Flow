package com.as2flow.application.views.drawer.identities;

import com.as2flow.application.backend.entity.Identity;
import com.as2flow.application.backend.service.IdentityService;
import com.as2flow.application.views.main.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "identities", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Identities")
public class IdentitiesView extends HorizontalLayout
{
    private final IdentityService identityService;
    private final Grid<Identity> grid = new Grid<>(Identity.class);

    public IdentitiesView(IdentityService identityService)
    {
        this.identityService = identityService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(grid);
        updateList();
    }

    private void updateList()
    {
        grid.setItems(identityService.findAll());
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
