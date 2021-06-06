package com.as2flow.views;

import com.as2flow.views.drawer.partnersips.IdentitiesView;
import com.as2flow.views.drawer.partnersips.PartnersView;
import com.as2flow.views.drawer.partnersips.PartnershipsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "AS2 Flow",
        shortName = "AS2 Flow",
        offlineResources = {
                "./styles/offline.css",
                "./images/offline.png"},
        enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./views/main/main-view.css")
public class MainView extends AppLayout
{
    private final Tabs menu;
    private H1 viewTitle;

    public MainView()
    {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget)
    {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Component createHeaderContent()
    {
        HorizontalLayout header = new HorizontalLayout();
        header.setId("header");
        header.getThemeList().set("dark", true);
        header.setWidthFull();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.add(new DrawerToggle());
        viewTitle = new H1();
        header.add(viewTitle);
        Anchor logoutAnchor = new Anchor("logout", "Log out");
        logoutAnchor.getElement().getStyle().set("margin-left", "auto");
        logoutAnchor.getElement().getStyle().set("margin-right", "1%");
        header.add(logoutAnchor);

        return header;
    }

    private Component createDrawerContent(Tabs menu)
    {
        VerticalLayout drawer = new VerticalLayout();
        drawer.setSizeFull();
        drawer.setPadding(false);
        drawer.setSpacing(false);
        drawer.getThemeList().set("spacing-s", true);
        drawer.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "AS2 Flow logo"));
        logoLayout.add(new H1("AS2 Flow"));
        drawer.add(logoLayout, menu);
        return drawer;
    }

    private Tabs createMenu()
    {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems()
    {
        return new Tab[]{
                createTab("Senders", IdentitiesView.class),
                createTab("Receivers", PartnersView.class),
                createTab("Partnerships", PartnershipsView.class)
        };
    }

    @Override
    protected void afterNavigation()
    {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component)
    {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle()
    {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
