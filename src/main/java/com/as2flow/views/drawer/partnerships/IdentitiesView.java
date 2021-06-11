package com.as2flow.views.drawer.partnerships;

import com.as2flow.backend.entity.Partnership;
import com.as2flow.backend.service.PartnershipService;
import com.as2flow.views.MainView;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.commons.collection.attr.IStringMap;
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
    private final PartnershipService partnershipService;
    private final Grid<Partnership> grid = new Grid<>(Partnership.class);
    private final TextField filterText = new TextField();

    public IdentitiesView(PartnershipService partnershipService)
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
        filterText.setPlaceholder("Filter by AS2 ID...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList()
    {
        grid.setItems(partnershipService.findAll(CPartnershipIDs.PID_AS2, filterText.getValue(), PartnershipService.FindBy.SenderAttrs));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(sender ->
        {
            IStringMap m = sender.getSenderAttrs();
            return m.getValue(CPartnershipIDs.PID_AS2);
        }).setHeader("AS2 ID");
        grid.addColumn(sender ->
        {
            IStringMap m = sender.getSenderAttrs();
            return m.getValue(CPartnershipIDs.PID_X509_ALIAS);
        }).setHeader("Key alias");
        grid.addColumn(sender ->
        {
            IStringMap m = sender.getSenderAttrs();
            String email = m.getValue(CPartnershipIDs.PID_EMAIL);
            return email == null ? "-" : email;
        }).setHeader("Email");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
