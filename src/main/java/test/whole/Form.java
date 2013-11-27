/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.whole;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class Form extends JFrame implements FocusListener {

    JTextField fname, lname, phone, email, address;
    BufferedImage img;

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Form();
            }
        });
    }

    public Form() {
        super("Form with Validation Capability");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(5, 3));

        try {
            img = ImageIO.read(new File("src/warning.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        add(new JLabel("First Name :"));
        add(fname = new JTextField(20));
        add(new JLabel("Last Name :"));
        add(lname = new JTextField(20));
        add(new JLabel("Phone Number :"));
        add(phone = new JTextField(10));
        add(new JLabel("Email:"));
        add(email = new JTextField(20));
        add(new JLabel("Address :"));
        add(address = new JTextField(20));

        fname.addFocusListener(this);
        lname.addFocusListener(this);
        phone.addFocusListener(this);
        email.addFocusListener(this);
        address.addFocusListener(this);

        setVisible(true);
    }

    public void focusGained(FocusEvent e) {
        ((JTextComponent) e.getSource()).setBackground(Color.WHITE);
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(fname)) {
            validationForText(fname);
        } else if (e.getSource().equals(lname)) {
            validationForText(lname);
        } else if (e.getSource().equals(email)) {
            validationForEmail(email);
        } else if (e.getSource().equals(phone)) {
            validationForNumber(phone);
        } else {
            ((JTextComponent) e.getSource()).setBackground(Color.GREEN);
        }
    }

    public void validationForText(JTextComponent comp) {
        String temp = comp.getText();
        if (temp.matches("^[A-Za-z]+$")) {
            comp.setBackground(Color.GREEN);
        } else {
            comp.setBackground(Color.RED);
        }
    }

    public void validationForNumber(JTextComponent comp) {
        String text = comp.getText();
        if (text.matches("^[0-9]+$")) {
            comp.setBackground(Color.GREEN);
        } else {
            comp.setBackground(Color.RED);
        }
    }

    public void validationForEmail(JTextComponent comp) {
        String text = comp.getText();
        if (text.matches("[^@]+@([^.]+\\.)+[^.]+")) {
            comp.setBackground(Color.GREEN);
        } else {
            comp.setBackground(Color.RED);
        }
    }

}
