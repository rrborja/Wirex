package net.wirex.gui;

import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class ErrorReportModel extends Model {

    private String appName;
    private Boolean details;
    private Boolean screenshot;

    public ErrorReportModel() {
        appName = "";
        details = true;
        screenshot = true;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String newValue) {
        String oldValue = appName;
        this.appName = newValue;
        changeSupport.firePropertyChange("appName", oldValue, newValue);
    }

    public Boolean getDetails() {
        return details;
    }

    public void setDetails(Boolean newValue) {
        Boolean oldValue = details;
        this.details = newValue;
        changeSupport.firePropertyChange("details", oldValue, newValue);
    }

    public Boolean getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(Boolean newValue) {
        Boolean oldValue = screenshot;
        this.screenshot = newValue;
        changeSupport.firePropertyChange("screenshot", oldValue, newValue);
    }

}
