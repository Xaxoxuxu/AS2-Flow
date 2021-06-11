package com.as2flow.views.drawer;

import com.as2flow.backend.entity.AS2MessageEntity;
import com.as2flow.backend.entity.PartnershipEntity;
import com.as2flow.backend.service.AS2MessageService;
import com.as2flow.views.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "monitoring", layout = MainView.class)
@PageTitle("Monitoring")
public class MonitoringView extends VerticalLayout
{
    private final AS2MessageService as2MessageService;
    private final Grid<AS2MessageEntity> grid = new Grid<>(AS2MessageEntity.class);
    private final TextField filterText = new TextField();

    public MonitoringView(AS2MessageService as2MessageService)
    {
        this.as2MessageService = as2MessageService;
        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();

        add(filterText, grid);
        updateList();
    }

    private void configureFilter()
    {
        filterText.setPlaceholder("Filter by partnership name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList()
    {
        grid.setItems(as2MessageService.findAll(filterText.getValue()));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(message ->
        {
            PartnershipEntity p = message.getPartnership();
            return p.getName();
        }).setHeader("Partnership Name");
        grid.addColumn(AS2MessageEntity::getTimeProcessed).setHeader("Time Processed");
    }
}
