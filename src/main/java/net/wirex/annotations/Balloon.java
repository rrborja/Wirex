package net.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Balloon {

    String text() default "";

    Class<? extends JComponent> value() default JPanel.class;

    int seconds() default 5;
}
