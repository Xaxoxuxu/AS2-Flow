package com.as2flow.application.views.drawer.identities;

import com.as2flow.application.backend.entity.Identity;
import com.as2flow.application.backend.service.IdentityService;
import com.as2flow.application.views.main.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "identities", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Identities")
public class IdentitiesView extends VerticalLayout
{
    private final IdentityService identityService;
    private final Grid<Identity> grid = new Grid<>(Identity.class);
    private final TextField filterText = new TextField();

    public IdentitiesView(IdentityService identityService)
    {
        this.identityService = identityService;
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
        grid.setItems(identityService.findAll(filterText.getValue()));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.setColumns("name", "as2Id");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
