package net.wirex.annotations;

import java.awt.Window;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fire {

    Class<? extends JPanel> view();

    Class<? extends Window> type() default JDialog.class;

    String title() default "Untitled";

    boolean depends() default false;

    boolean dynamic() default true;
}
