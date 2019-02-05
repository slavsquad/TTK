package com.slavsquad.TTK.Forms;

import com.slavsquad.TTK.Text.Converter;
import com.slavsquad.TTK.Text.Dictionaries.Dictionaries;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Class <>ConversionProgressForm</> implements window for displaying progress conversion file in dictionary
 */
public class ConversionProgressForm extends JDialog {

    JProgressBar conversionProgressBar;
    JButton closeBtn;
    JPanel mainPanel;
    private BoundedRangeModel progressBarBRM;
    File file;

    /**
     * Construct new instance class
     * @param parent form
     * @param title conbersion's form
     * @param dictionaries where adding new dictionary
     * @param file from which reading text for new dictionary
     * @param MAX - quantity of characters
     * @param sizeText - size of text for dictionary(the dictionary contains any texts)
     */
    ConversionProgressForm(JFrame parent, String title, Dictionaries dictionaries, File file, final int MAX, int sizeText){
        super(parent,title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        UIManager.put("ProgressBar.cellSpacing", 2);
        UIManager.put("ProgressBar.cellLength", 6);

        this.file = file;

        progressBarBRM = new DefaultBoundedRangeModel(0,0,0,MAX);
        conversionProgressBar = new JProgressBar(progressBarBRM);
        conversionProgressBar.setForeground(Color.green);
        progressBarBRM.addChangeListener(e -> {
            if (progressBarBRM.getValue()==MAX){
                closeBtn.setEnabled(true);
            }
        });
        closeBtn = new JButton("Close");
        closeBtn.setEnabled(false);
        closeBtn.addActionListener(e -> dispose());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(new JLabel("Conversion text file in dictionaryLabel:"), new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0));
        mainPanel.add(conversionProgressBar, new GridBagConstraints(0,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0));
        mainPanel.add(closeBtn, new GridBagConstraints(0,2,1,1,0,0, GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0));

        add(mainPanel);

        new Converter(file,dictionaries,sizeText,progressBarBRM).execute();

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
}
