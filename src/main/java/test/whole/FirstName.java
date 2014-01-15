/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.whole;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author lcairel
 */
@Documented
@Constraint(validatedBy = FirstNameConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstName {
    String message() default "first_name";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
