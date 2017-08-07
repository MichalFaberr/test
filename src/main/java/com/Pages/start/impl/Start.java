package com.Pages.start.impl;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;

import com.vaadin.ui.*;


@SpringUI
//@Theme("dark-flat")
class Start extends UI{

    private HorizontalLayout _layoutMain;

    @Override
    protected void init(VaadinRequest request) {
        _layoutMain = new HorizontalLayout();
        this.setContent(_layoutMain);
        _layoutMain.setSizeFull();

        _layoutMain.addComponent(new Label("Test..."));

        this.setSizeFull();
    }


}
