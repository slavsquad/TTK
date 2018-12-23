package com.slavsquad.TTK;

import com.slavsquad.TTK.Forms.MainForm;

import javax.swing.*;

/**
 * Class <>Main</> implements entry point in program
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainForm::new);
    }
}
