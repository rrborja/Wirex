/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.Date;
import net.visp.wirex.interfaces.Model;

/**
 *
 * @author jvallar
 */
public class Item{
    private String itemCode;
    private String type;
    private String description;
    private String recur;
    private String expires;
    private String pricing;
    private String tax;
    private Date effectiveDate;
    private String Active;

    public Item(String itemCode, String type, String description, String recur, String expires, String pricing, String tax, Date effectiveDate, String Active) {
        this.itemCode = itemCode;
        this.type = type;
        this.description = description;
        this.recur = recur;
        this.expires = expires;
        this.pricing = pricing;
        this.tax = tax;
        this.effectiveDate = effectiveDate;
        this.Active = Active;
    }
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecur() {
        return recur;
    }

    public void setRecur(String recur) {
        this.recur = recur;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }
    
}
