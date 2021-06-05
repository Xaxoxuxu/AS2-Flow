package com.as2flow.application.views.drawer.partnersips;

import com.as2flow.application.backend.entity.Partnership;
import com.as2flow.application.backend.service.PartnershipService;
import com.as2flow.application.views.MainView;
import com.as2flow.application.views.PartnershipForm;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.commons.collection.attr.IStringMap;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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

        configureFilter();
        configureGrid();

        partnershipForm = new PartnershipForm();
        Div content = new Div(grid, partnershipForm);
        content.addClassName("content");
        content.setSizeFull();

        add(filterText, content);
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
        grid.removeAllColumns();
        grid.addColumn(Partnership::getName).setHeader("Name");
        grid.addColumn(partnership -> {
            IStringMap m = partnership.getAttributes();
            return m.getValue(CPartnershipIDs.PA_SUBJECT);
        }).setHeader("Subject");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
