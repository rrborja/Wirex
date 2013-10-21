/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.annotations;

import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

/**
 *
 * @author RBORJA
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    Class<? extends EventListener> type() default ActionListener.class;
    String[] value() default {"run"};
}
