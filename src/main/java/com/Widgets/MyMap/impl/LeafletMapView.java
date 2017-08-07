package com.Widgets.MyMap.impl;

import com.Entities.GpsEntity;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.*;
import org.vaadin.addon.leaflet.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by michal on 23.04.17.
 */
public class LeafletMapView extends VerticalLayout {

    private MyMapModel _myMapModel;

    private LMap _lmap;
    private int _idGpsLast;
    private LMarker _lMarkerLastGps;

    private List<LMarker> _listMarkers;
    private Registration _registration;

    private Button _buttonRefresh;

    private Window _windowSettings;

    public LeafletMapView(MyMapModel myMapModel) throws Exception {

        if (myMapModel == null)
            throw new Exception("public LeafletMapView( MyMapModel myMapModel) throws Exception : MyMapModel equals to null.");

        _myMapModel = myMapModel;

        _lmap = new LMap();
        _lmap.setSizeFull();
        GpsEntity gpsLast = _myMapModel.getGpsLast();
        _lmap.setCenter(gpsLast.getLatitude(), gpsLast.getLongitude());
        _lmap.setZoomLevel(_myMapModel.get_zoomValue());

        this.setSizeFull();
        this.addComponent(_lmap);

        LOpenStreetMapLayer lOpenStreetMapLayer = new LOpenStreetMapLayer();
        //lOpenStreetMapLayer.setNoWrap(true); // default false
        _lmap.addBaseLayer(lOpenStreetMapLayer, "Open Street Map Layer");
        lOpenStreetMapLayer.bringToBack();

        _lMarkerLastGps = new LMarker(0, 0);
        _idGpsLast = -1;
        _listMarkers = new ArrayList<>();

        /*
        //Should cross pacific ocean
        LPolyline lPolyline = new LPolyline(new Point(36.261703, -75.497002), new Point(50.999784, 1.519677));
        _lmap.addLayer(lPolyline);
        lPolyline.bringToFront();
        */

        GridLayout layoutBaseSettings = new GridLayout(3, 1);
        layoutBaseSettings.setSizeFull();
        layoutBaseSettings.setSpacing(true);
        this.addComponent(layoutBaseSettings);

        setupAdvancedSettings(layoutBaseSettings);
        setupBaseSettings(layoutBaseSettings);
        this.setExpandRatio(_lmap, 1);
        this.setExpandRatio(layoutBaseSettings, 0.1f);
    }

    private void setupBaseSettings(GridLayout layout) {
        try {
            setupButtonRefresh(layout);
            setupCheckBoxAutoRefresh(layout);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    private void setupButtonRefresh(GridLayout layoutBaseSettings) {

        _buttonRefresh = new Button("Refresh");
        _buttonRefresh.setSizeFull();
        _buttonRefresh.addClickListener(listener -> refresh());

        layoutBaseSettings.addComponent(_buttonRefresh);

    }

    private void setupCheckBoxAutoRefresh(GridLayout layoutSettings) {
        CheckBox checkBoxAutoRefresh = new CheckBox("Auto Refresh", false);
        checkBoxAutoRefresh.addValueChangeListener(listener -> manageAutoRefreshMap(listener.getValue()));

        layoutSettings.addComponent(checkBoxAutoRefresh);
        layoutSettings.setComponentAlignment(checkBoxAutoRefresh, Alignment.MIDDLE_CENTER);

    }

    private void setupAdvancedSettings(GridLayout layout) {

        try {

            Button buttonSettings = new Button("Settings");
            buttonSettings.setSizeFull();
            buttonSettings.addClickListener(listener -> {

                getUI().removeWindow(_windowSettings);
                _windowSettings = null;
                _windowSettings = new Window("Settings");
                _windowSettings.setHeight("100%");
                _windowSettings.setWidth("30%");
                _windowSettings.setPosition(1, 1);
                _windowSettings.setDraggable(false);
                getUI().addWindow(_windowSettings);

                Panel panelSettings = new Panel();
                panelSettings.setSizeFull();

                VerticalLayout layoutSettings = new VerticalLayout();
                layoutSettings.setCaption("Settings");
                layoutSettings.setSizeUndefined();
                layoutSettings.setMargin(true);
                layoutSettings.setSpacing(true);

                setupTimeToRefresh(layoutSettings);
                setupAutoCenter(layoutSettings);
                setupRadioBoxOneOrAll(layoutSettings);
                setupGpsFromDate(layoutSettings);
                setupUpdateTtn(layoutSettings);

                panelSettings.setContent(layoutSettings);
                _windowSettings.setContent(panelSettings);

            });

            layout.addComponent(buttonSettings);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void setupTimeToRefresh(Layout layout) {

        HorizontalLayout layoutRefresh = new HorizontalLayout();
        layoutRefresh.setCaption("Refresh");
        layoutRefresh.setSpacing(true);
        Label description = new Label("Time to auto refresh: ");
        layoutRefresh.addComponent(description);

        List<String> data = new ArrayList<>();
        data.add("1 s");
        for (int i = 5; i <= 60; i += 5)
            data.add(i + " s");

        NativeSelect selectZoom = new NativeSelect<>(null, data);
        selectZoom.setSizeFull();
        selectZoom.setEmptySelectionAllowed(false);
        selectZoom.setSelectedItem("5 s");
        selectZoom.addValueChangeListener(listener -> {
            String selectedItem = listener.getValue().toString();
            String value = selectedItem.substring(0, selectedItem.indexOf(' '));
            _myMapModel.set_timeToRefreshMs(1000 * (int) Double.parseDouble(value));
            UI myUI = UI.getCurrent();
            myUI.setPollInterval(_myMapModel.get_timeToRefreshMs());
        });

        layoutRefresh.addComponent(selectZoom);
        layoutRefresh.setComponentAlignment(selectZoom, Alignment.TOP_LEFT);
        layoutRefresh.setComponentAlignment(description, Alignment.TOP_LEFT);

        layout.addComponent(layoutRefresh);
    }

    private void setupAutoCenter(VerticalLayout layout) {

        try {

            HorizontalLayout layoutAutoCenter = new HorizontalLayout();
            layoutAutoCenter.setCaption("Auto center and zoom level");

            CheckBox checkBoxCenter = new CheckBox("Enable", _myMapModel.is_autoCenter());
            checkBoxCenter.setSizeFull();
            checkBoxCenter.addValueChangeListener(listener -> _myMapModel.set_autoCenter(listener.getValue()));

            List<String> data = new ArrayList<>();
            for (int i = 1; i <= 20; ++i)
                data.add("Zoom: " + i);

            NativeSelect selectZoom = new NativeSelect<>(null, data);
            selectZoom.setSizeFull();
            selectZoom.setEmptySelectionAllowed(false);
            selectZoom.setSelectedItem(data.get((int) _myMapModel.get_zoomValue() - 1));
            selectZoom.addValueChangeListener(listener -> {
                String selectedItem = listener.getValue().toString();
                String value = selectedItem.substring(selectedItem.indexOf(' '));
                _myMapModel.set_zoomValue(Double.parseDouble(value));
            });

            layoutAutoCenter.addComponent(checkBoxCenter);
            layoutAutoCenter.addComponent(selectZoom);
            layoutAutoCenter.setComponentAlignment(checkBoxCenter, Alignment.MIDDLE_CENTER);

            layout.addComponent(layoutAutoCenter);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void setupRadioBoxOneOrAll(Layout layout) {

        try {
            List<String> data = Arrays.asList("Last gps", "All gps");

            RadioButtonGroup radioButtonsRefreshOneOrAll = new RadioButtonGroup<>("Show", data);
            radioButtonsRefreshOneOrAll.setSizeFull();

            radioButtonsRefreshOneOrAll.setItemCaptionGenerator(item -> (String) item);

            if (_myMapModel.get_gpsSelect() == MyMapModel.GPS_SELECT.ALL)
                radioButtonsRefreshOneOrAll.setSelectedItem(data.get(1));
            else
                radioButtonsRefreshOneOrAll.setSelectedItem(data.get(0));

            radioButtonsRefreshOneOrAll.setItemEnabledProvider(item -> true);

            radioButtonsRefreshOneOrAll.addValueChangeListener(event -> {

                if (radioButtonsRefreshOneOrAll.getSelectedItem().get().equals("Last gps"))
                    _myMapModel.set_gpsSelect(MyMapModel.GPS_SELECT.LAST);
                else
                    _myMapModel.set_gpsSelect(MyMapModel.GPS_SELECT.ALL);

            });

            layout.addComponent(radioButtonsRefreshOneOrAll);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    private void setupGpsFromDate(VerticalLayout layout) {

        HorizontalLayout layoutFromDate = new HorizontalLayout();
        layoutFromDate.setSpacing(true);
        layoutFromDate.setSizeUndefined();
        layoutFromDate.setCaption("Show from date");

        InlineDateTimeField dateTimeField = new InlineDateTimeField();
        dateTimeField.setEnabled(_myMapModel.get_fromDateEnabled());
        dateTimeField.setValue(LocalDateTime.now());
        dateTimeField.setLocale(Locale.GERMAN);
        dateTimeField.setResolution(DateTimeResolution.MINUTE);
        _myMapModel.setFromDate(dateTimeField.getValue());

        Label labelDate = new Label("");
        labelDate.setValue(_myMapModel.get_fromDate().toString());

        dateTimeField.addValueChangeListener(event -> {
            _myMapModel.setFromDate(event.getValue());
            labelDate.setValue(_myMapModel.get_fromDate().toString());
        });

        CheckBox checkBoxFromDateEnable = new CheckBox("Enable", _myMapModel.get_fromDateEnabled());
        checkBoxFromDateEnable.addValueChangeListener(event -> {
            _myMapModel.setFromDate(dateTimeField.getValue());
            _myMapModel.set_fromDateEnabled(event.getValue());
            dateTimeField.setEnabled(event.getValue());
        });

        layoutFromDate.addComponent(checkBoxFromDateEnable);
        layoutFromDate.addComponent(labelDate);
        layout.addComponent(layoutFromDate);
        layout.addComponent(dateTimeField);
        layout.setComponentAlignment(dateTimeField, Alignment.TOP_CENTER);
    }

    private void setupUpdateTtn(VerticalLayout layout) {

        GridLayout gridLayoutTtnEnable = new GridLayout(1, 2);
        gridLayoutTtnEnable.setCaption("The Things Network");
        gridLayoutTtnEnable.setSpacing(true);

        GridLayout gridLayoutTtnSettings = new GridLayout(2, 4);
        gridLayoutTtnSettings.setSpacing(true);
        TextField textFieldAppKey = new TextField("AppKey");
        TextField textFieldAppId = new TextField("AppId");
        TextField textFieldAppRegion = new TextField("AppRegion");
        TextField textFieldDeviceId = new TextField("DeviceId");
        gridLayoutTtnSettings.addComponent(textFieldAppKey, 0, 0);
        gridLayoutTtnSettings.addComponent(textFieldAppId, 0, 1);
        gridLayoutTtnSettings.addComponent(textFieldAppRegion, 0, 2);
        gridLayoutTtnSettings.addComponent(textFieldDeviceId, 0, 3);

        Button buttonApply = new Button("Apply");
        buttonApply.setSizeFull();
        buttonApply.addClickListener(listener -> {

            if (_myMapModel.updateTtn(
                    textFieldAppKey.getValue(),
                    textFieldAppId.getValue(),
                    textFieldAppRegion.getValue(),
                    textFieldDeviceId.getValue()
            ))
                Notification.show("Updated");
            else
                Notification.show("Update Error");

            textFieldAppKey.clear();
            textFieldAppId.clear();
            textFieldAppRegion.clear();
            textFieldDeviceId.clear();
        });

        CheckBox checkBoxEnableTtnSettings = new CheckBox("Enable", _myMapModel.get_enableTtnSettings());
        gridLayoutTtnSettings.setEnabled(_myMapModel.get_enableTtnSettings());
        checkBoxEnableTtnSettings.addValueChangeListener(listener -> {

            if (listener.getValue() == true) {

                PasswordField textFieldPassword = new PasswordField();
                textFieldPassword.setCaption("Password");

                Button buttonConfirmPassword = new Button("Confirm");

                VerticalLayout horizontalLayoutPassword = new VerticalLayout();
                horizontalLayoutPassword.setCaption("Password");
                horizontalLayoutPassword.setSpacing(true);
                horizontalLayoutPassword.setWidth("100%");
                horizontalLayoutPassword.setHeightUndefined();

                horizontalLayoutPassword.addComponent(textFieldPassword);
                horizontalLayoutPassword.addComponent(buttonConfirmPassword);
                horizontalLayoutPassword.setComponentAlignment(textFieldPassword, Alignment.MIDDLE_CENTER);
                horizontalLayoutPassword.setComponentAlignment(buttonConfirmPassword, Alignment.MIDDLE_CENTER);

                Window window = new Window("Type password to unlock");
                window.setClosable(false);
                window.setWidth("25%");
                window.setHeight("35%");
                Panel panelTtnPassword = new Panel(null, horizontalLayoutPassword);
                panelTtnPassword.setSizeFull();
                window.setContent(panelTtnPassword);
                window.setModal(true);
                window.center();

                buttonConfirmPassword.addClickListener(listenerButton -> {

                    Notification notificationWasPasswordCorrect = new Notification("Password", Notification.TYPE_WARNING_MESSAGE);
                    notificationWasPasswordCorrect.setDelayMsec(5000);

                    if (textFieldPassword.getValue().equals("pwrbalon000")) {
                        _myMapModel.set_enableTtnSettings(true);
                        notificationWasPasswordCorrect.setDescription("Correct");
                    } else {
                        checkBoxEnableTtnSettings.setValue(false);
                        notificationWasPasswordCorrect.setDescription("Incorrect!!!");
                    }

                    notificationWasPasswordCorrect.show(getUI().getPage());

                    gridLayoutTtnSettings.setEnabled(_myMapModel.get_enableTtnSettings());

                    window.close();
                });

                getUI().addWindow(window);
                window.bringToFront();

            } else {
                _myMapModel.set_enableTtnSettings(false);
            }

            gridLayoutTtnSettings.setEnabled(_myMapModel.get_enableTtnSettings());
        });

        gridLayoutTtnSettings.addComponent(buttonApply, 1, 0, 1, 3);
        gridLayoutTtnSettings.setComponentAlignment(buttonApply, Alignment.MIDDLE_CENTER);
        gridLayoutTtnEnable.addComponent(checkBoxEnableTtnSettings);
        gridLayoutTtnEnable.addComponent(gridLayoutTtnSettings);
        layout.addComponent(gridLayoutTtnEnable);
    }

    private void manageAutoRefreshMap(Boolean isAutoRefreshChecked) {
        try {

            if (_registration != null) {
                _registration.remove();
                _registration = null;
            }

            if (isAutoRefreshChecked) {

                _buttonRefresh.setEnabled(false);

                UI myUI = UI.getCurrent();
                myUI.setPollInterval(_myMapModel.get_timeToRefreshMs());
                _registration = myUI.addPollListener(event -> refresh());

            } else {
                _buttonRefresh.setEnabled(true);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void refresh() {
        refreshMap();
        refreshAutoCenter();
    }

    private void refreshAutoCenter() {
        try {

            if (_myMapModel.is_autoCenter()) {
                if (_myMapModel.get_gpsSelect() == MyMapModel.GPS_SELECT.ALL) {
                    _lmap.setCenter(_listMarkers.get(_listMarkers.size() - 1).getPoint(), _myMapModel.get_zoomValue());
                } else {
                    _lmap.setCenter(_lMarkerLastGps.getPoint(), _myMapModel.get_zoomValue());
                }
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void refreshMap() {
        if (_myMapModel.get_gpsSelect() == MyMapModel.GPS_SELECT.LAST) {
            refreshLastGps();
        } else {
            refreshAllGps();
        }
    }

    private void refreshLastGps() {

        if (_myMapModel != null) {

            try {

                for (LMarker lMarker : _listMarkers) {
                    _lmap.removeComponent(lMarker);
                }

                _listMarkers.clear();

                GpsEntity gpsEntityNewest = _myMapModel.getGpsLast();

                if (_idGpsLast != gpsEntityNewest.getIdgps()) {
                    _idGpsLast = gpsEntityNewest.getIdgps();
                    _lmap.removeComponent(_lMarkerLastGps);
                    _lMarkerLastGps = null;

                    _lMarkerLastGps = new LMarker(gpsEntityNewest.getLatitude(), gpsEntityNewest.getLongitude());
                    _lMarkerLastGps.setCaption(Integer.toString(gpsEntityNewest.getIdgps()));
                    _lMarkerLastGps.setPopup(
                            getCopyableContent("Date: " + gpsEntityNewest.getDate().toString()) +
                                    getCopyableContent("Latitude: " + gpsEntityNewest.getLatitude()) +
                                    getCopyableContent("Longitude: " + gpsEntityNewest.getLongitude()) +
                                    getCopyableContent("Altitude: " + gpsEntityNewest.getAltitude() + " m")
                    );
                    _lMarkerLastGps.setIcon(FontAwesome.ROCKET);
                    _lmap.addComponent(_lMarkerLastGps);
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }

        }
    }

    private void refreshAllGps() {
        if (_myMapModel != null) {

            try {

                GpsEntity gpsEntities[] = _myMapModel.getGpsAll();

                if (_listMarkers.size() == gpsEntities.length) {
                    if (gpsEntities[gpsEntities.length - 1].getIdgps() == Integer.parseInt(_listMarkers.get(_listMarkers.size() - 1).getCaption())) {
                        return;
                    }
                }

                for (LMarker lMarker : _listMarkers) {
                    _lmap.removeComponent(lMarker);
                }

                _listMarkers.clear();

                for (int i = 0; i < gpsEntities.length - 1; ++i) {
                    GpsEntity gpsEntity = gpsEntities[i];
                    LMarker lMarkerGps = new LMarker(gpsEntity.getLatitude(), gpsEntity.getLongitude());
                    lMarkerGps.setCaption(Integer.toString(gpsEntity.getIdgps()));
                    lMarkerGps.setPopup(
                            getCopyableContent("Date: " + gpsEntity.getDate().toString()) +
                                    getCopyableContent("Latitude: " + gpsEntity.getLatitude()) +
                                    getCopyableContent("Longitude: " + gpsEntity.getLongitude()) +
                                    getCopyableContent("Altitude: " + gpsEntity.getAltitude() + " m")
                    );
                    _listMarkers.add(lMarkerGps);
                    _lmap.addComponent(lMarkerGps);
                }

                GpsEntity gpsEntity = gpsEntities[gpsEntities.length - 1];
                LMarker lMarkerLastGps = new LMarker(gpsEntity.getLatitude(), gpsEntity.getLongitude());
                lMarkerLastGps.setCaption(Integer.toString(gpsEntity.getIdgps()));
                lMarkerLastGps.setPopup(
                        getCopyableContent("Date: " + gpsEntity.getDate().toString()) +
                                getCopyableContent("Latitude: " + gpsEntity.getLatitude()) +
                                getCopyableContent("Longitude: " + gpsEntity.getLongitude()) +
                                getCopyableContent("Altitude: " + gpsEntity.getAltitude() + " m")
                );
                _listMarkers.add(lMarkerLastGps);
                lMarkerLastGps.setIcon(FontAwesome.ROCKET);
                _lmap.addComponent(lMarkerLastGps);


            } catch (NullPointerException e) {
                Notification notif = new Notification(
                        "Warning",
                        "Empty array of gps entities!",
                        Notification.TYPE_WARNING_MESSAGE);

                notif.setDelayMsec(5000);
                notif.setPosition(Position.MIDDLE_CENTER);
                notif.show(Page.getCurrent());

            } catch (Exception e) {
                System.err.println(e.toString());
            }

        }
    }

    private String getCopyableContent(String content) {
        return "<div class='infowindow-content' style='user-select: text !important'>" + content + "</div>";
    }

    @Override
    protected void finalize() throws Throwable {

        if (_registration != null) {
            _registration.remove();
            _registration = null;
        }
    }
}
