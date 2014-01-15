/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.whole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author lcairel
 */
public class FirstNameConstraintValidator implements ConstraintValidator<FirstName, String>  {
    
   public static final String SYMBOLS = "^([a-zA-Z0-9\\\\&\\\\/]{1}[a-zA-Z0-9-&,.'/]{0,29})?$";

    @Override
    public void initialize(FirstName a) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {
        return value.matches(SYMBOLS);
    }

}
