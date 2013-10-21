/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.visp.wirex.interfaces.Model;
import net.visp.wirex.interfaces.Presenter;

/**
 *
 * @author RBORJA
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
    Class<? extends Model> model();
    Class<? extends Presenter> presenter();
}
