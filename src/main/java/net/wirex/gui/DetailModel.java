package net.wirex.gui;

import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class DetailModel extends Model {
 
    private String comment;
    private String report;
    private String organization;
    private String email;
    private String reporter;

    public DetailModel() {
        comment = "";
        report = "";
        organization = "";
        email = "";
        reporter = "";
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newValue) {
        String oldValue = comment;
        this.comment = newValue;
        changeSupport.firePropertyChange("comment", oldValue, newValue);
    }

    public String getReport() {
        return report;
    }

    public void setReport(String newValue) {
        String oldValue = report;
        this.report = newValue;
        changeSupport.firePropertyChange("report", oldValue, newValue);
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String newValue) {
        String oldValue = organization;
        this.organization = newValue;
        changeSupport.firePropertyChange("organization", oldValue, newValue);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newValue) {
        String oldValue = email;
        this.email = newValue;
        changeSupport.firePropertyChange("email", oldValue, newValue);
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String newValue) {
        String oldValue = reporter;
        this.reporter = newValue;
        changeSupport.firePropertyChange("report", oldValue, newValue);
    }
    
    
    
}
