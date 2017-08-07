import com.vaadin.server.WidgetsetInfo;

/* Addons in this ws:
 * 8.0.4
 * OBF
 * v-leaflet:org.vaadin.addon:2.0.0
 * switch:org.vaadin.teemu:3.0.0
 * refresher:org.vaadin.addons:1.2.3.7
 * googlemaps:com.vaadin.tapio:2.0.0

 */
public class AppWidgetset implements WidgetsetInfo {

    @Override
    public String getWidgetsetName() {
        return "ws7c5e539c434717a1388d2b7738d430e7";
    }

    @Override
    public String getWidgetsetUrl() {
        return "https://ws.vaadin.com/ws7c5e539c434717a1388d2b7738d430e7/ws7c5e539c434717a1388d2b7738d430e7.nocache.js";
    }

    @Override
    public boolean isCdn() {
        return !true;
    }

}
