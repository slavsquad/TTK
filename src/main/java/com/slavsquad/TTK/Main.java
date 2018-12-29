package com.slavsquad.TTK;

import com.slavsquad.TTK.Forms.MainForm;

import javax.swing.*;

/**
 * Class <>Main</> implements entry point in program
 */
public class Main {
    public static void main(String[] args) {
        //Lambda expression
        SwingUtilities.invokeLater(MainForm::new);

        //Old version run
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainForm();
            }
        });*/
    }
}
