/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ritchie
 */
class ConsoleProcess extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleProcess.class.getSimpleName());

    private final PipedInputStream pin = new PipedInputStream();
    private final PipedInputStream pin2 = new PipedInputStream();

    private Thread reader;
    private Thread reader2;

    private boolean quit;

    protected static String systemUser = "";

    public static int ycursor = 0;

    public ConsoleProcess(String name) {
        super(name);
    }

    public void initSystem() {
//        screen.getScreen().startScreen();
        try {
            PipedOutputStream pout = new PipedOutputStream(this.pin);
            System.setOut(new PrintStream(pout, true));
        } catch (java.io.IOException io) {
        } catch (SecurityException se) {
        }
        try {
            PipedOutputStream pout2 = new PipedOutputStream(this.pin2);
            System.setErr(new PrintStream(pout2, true));
        } catch (java.io.IOException io) {
        } catch (SecurityException se) {
        }
        quit = false;

        reader = new SystemIOThread();
        reader.setDaemon(true);
        reader.start();

        reader2 = new SystemIOThread();
        reader2.setDaemon(true);
        reader2.start();
    }

    @Override
    public void run() {
//        initSystem();
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
    

    public class SystemIOThread extends Thread {

        @Override
        public synchronized void run() {
            try {
                while (Thread.currentThread() == reader) {
                    try {
                        this.wait(100);
                    } catch (InterruptedException ie) {
                    }
                    if (pin.available() != 0) {
                        String input = this.readLine(pin);

//                        ScreenWriter writer = new ScreenWriter(screen.getScreen());
//                        writer.drawString(0, ycursor++, input);
//                        screen.getScreen().refresh();
                    }
                    if (quit) {
                        return;
                    }
                }

                while (Thread.currentThread() == reader2) {
                    try {
                        this.wait(100);
                    } catch (InterruptedException ie) {
                    }
                    if (pin2.available() != 0) {
                        String input = this.readLine(pin2);
//                        ScreenWriter writer = new ScreenWriter(screen.getScreen());
//                        writer.drawString(0, ycursor++, input);
//                        screen.getScreen().refresh();
                    }
                    if (quit) {
                        return;
                    }
                }
            } catch (IOException e) {

            }

        }

        public synchronized String readLine(PipedInputStream in) throws IOException {
            String input = "";
            do {
                int available = in.available();
                if (available == 0) {
                    break;
                }
                byte b[] = new byte[available];
                in.read(b);
                input = input + new String(b, 0, b.length);
            } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
            return input;
        }

    }



}
