package com.Pages.start.impl;

import com.Entities.FlyEntity;
import com.Entities.GpsEntity;
import com.Tools.CommunicationTools;

import com.Widgets.MyMap.impl.LeafletMapView;
import com.Widgets.MyMap.impl.MyMapModel;
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

        setupLeafletMap();

        this.setSizeFull();
    }

    private void setupLeafletMap() {

        try{
            LeafletMapView leafletMapView = new LeafletMapView(new MyMapModel(1));
            leafletMapView.setSizeFull();

            Panel panelLeafletMap = new Panel();
            panelLeafletMap.setContent(leafletMapView);
            panelLeafletMap.setSizeFull();
            _layoutMain.addComponent(panelLeafletMap);

        }catch (Exception e){
            UI.getCurrent().addWindow(new Window("Error", new Label("Start : private void setupLeafletMap() -> leafletMapView exception")));
        }

    }

    private void setupLayoutTestDataFromRest() {

        _layoutMain.removeAllComponents();

        try{

            _layoutMain.addComponent(new Label("Gps from objects:"));

            for(GpsEntity g : CommunicationTools.getGpsAllIdFly(1)){
                _layoutMain.addComponent(new Label(g.toString()));
            }

            _layoutMain.addComponent(new Label("Fly from objects:"));

            for (FlyEntity f : CommunicationTools.getFlyAll() ) {
                _layoutMain.addComponent(new Label(f.toString()));
            }

            _layoutMain.addComponent(new Label("Fly by id=2"));
            _layoutMain.addComponent(new Label(CommunicationTools.getFlyById(2).toString()));

        }catch (Exception e){

        }


    }
}
