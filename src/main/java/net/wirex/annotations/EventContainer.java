package net.wirex.annotations;

import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EventContainers.class)
public @interface EventContainer {
    Class<? extends EventListener> listens() default ActionListener.class;
    Event[] events();
}
