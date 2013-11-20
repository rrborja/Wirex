package net.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.wirex.EventMethod;
import static net.wirex.EventMethod.ACTION_PERFORMED;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Events.class)
public @interface Event {
    String value() default "run";
    EventMethod at() default ACTION_PERFORMED;
}
