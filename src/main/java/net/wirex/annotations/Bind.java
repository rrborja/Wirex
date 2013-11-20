package net.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
    Class<? extends Model> model();
    Class<? extends Presenter> presenter();
}
