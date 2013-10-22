/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.wirex.interfaces.Model;

/**
 *
 * @author RBORJA
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Form {
    Class<? extends Model> value();
}
