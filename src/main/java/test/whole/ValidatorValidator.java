/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.whole;

import net.wirex.interfaces.Validator;

/**
 * 
 * @author ritchie
 */
public class ValidatorValidator implements Validator {
    public final String username = "^[a-z0-9_-]{3,15}$";
    public final String password = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,30}$";
    public final String retype = password;
    public final String email = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
    public final String phoneNumber = "^(1?(-?\\d{3})-?)?(\\d{3})(-?\\d{4})$";

}
