package com.Pages.start.impl;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
//@Theme("dark-flat")
class Start extends UI{

    private HorizontalLayout _layoutMain;

    @Override
    protected void init(VaadinRequest request)
    {
        _layoutMain = new HorizontalLayout();
        _layoutMain.setWidth("100%");
        _layoutMain.setHeightUndefined();

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.setContent(_layoutMain);
        this.setContent(panel);

        setup();

        this.setSizeFull();
    }

    private void setup()
    {
        _layoutMain.addComponent(new Label("Test..."));
        VerticalLayout layout = new VerticalLayout();
        _layoutMain.addComponent(layout);
        for (int i = 0; i<100; ++i)
            layout.addComponent(new Label("Test..."));
        _layoutMain.addComponent(new Label("Test..."));

    }


}
