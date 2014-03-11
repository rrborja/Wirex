package net.wirex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.ConstraintValidator;

/**
 *
 * @author Ritchie Borja
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    
    Class<? extends ConstraintValidator> value();
    
    String length() default "100";
    
}
