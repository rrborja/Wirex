/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

/**
 *
 * @author Ritchie Borja
 */
public interface ActionTrigger {

    void run();
    
    void run(Object... args);
}
