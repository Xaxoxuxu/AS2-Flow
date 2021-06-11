package com.as2flow.views.drawer.partnerships;

import com.as2flow.backend.entity.PartnershipEntity;
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

@Route(value = "partners", layout = MainView.class)
@PageTitle("Partners")
public class PartnersView extends VerticalLayout
{
    private final PartnershipService partnershipService;
    private final Grid<PartnershipEntity> grid = new Grid<>(PartnershipEntity.class);
    private final TextField filterText = new TextField();

    public PartnersView(PartnershipService partnershipService)
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
        grid.setItems(partnershipService.findAll(CPartnershipIDs.PID_AS2, filterText.getValue(), PartnershipService.FindBy.ReceiverAttrs));
    }

    private void configureGrid()
    {
        grid.addClassName("entity-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(receiver ->
        {
            IStringMap m = receiver.getReceiverAttrs();
            return m.getValue(CPartnershipIDs.PID_AS2);
        }).setHeader("AS2 ID");
        grid.addColumn(receiver ->
        {
            IStringMap m = receiver.getReceiverAttrs();
            return m.getValue(CPartnershipIDs.PID_X509_ALIAS);
        }).setHeader("Key alias");
        grid.addColumn(receiver ->
        {
            IStringMap m = receiver.getReceiverAttrs();
            String email = m.getValue(CPartnershipIDs.PID_EMAIL);
            return email == null ? "-" : email;
        }).setHeader("Email");
        grid.addColumn(receiver ->
        {
            IStringMap m = receiver.getAttributes();
            String url = m.getValue(CPartnershipIDs.PA_AS2_URL);
            return url == null ? "-" : url;
        }).setHeader("URL");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }
}
