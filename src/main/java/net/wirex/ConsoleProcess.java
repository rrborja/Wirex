/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.util.Scanner;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ritchie
 */
public class ConsoleProcess extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleProcess.class.getSimpleName());

    protected static String systemUser;

    public ConsoleProcess(String name) {
        super(name);
        this.systemUser = "";
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String command = sc.nextLine();
            if (!systemUser.equals("") || command.equals("login") || command.equals("quit")) {
                String commands[] = command.split(" ");
                String params[] = new String[commands.length - 1];
                for (int i = 1; i < commands.length; i++) {
                    String string = commands[i];
                    params[i - 1] = string;
                }
                ConsoleEngine.execute(commands[0].toLowerCase(), params);
            } else {
                LOG.error("You must login first in order to execute {}", command);
                ConsoleEngine.execute("login");
            }
        }
    }

}
