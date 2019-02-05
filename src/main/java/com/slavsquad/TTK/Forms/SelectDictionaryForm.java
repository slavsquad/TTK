package com.slavsquad.TTK.Forms;

import com.slavsquad.TTK.Text.Dictionaries.Dictionaries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class <>SelectDictionaryForm</> implements form for select dictionaryLabel
 */
public class SelectDictionaryForm extends JDialog {
    Dictionaries dictionaries;
    JComboBox<String> namesDictionaries;
    JButton selectBtn, cancelBtn;
    JPanel mainPanel,gridPanelBtn,statusBar;
    JLabel dictionaryLabel;
    DefaultComboBoxModel<String> namesDictionariesCBM;

    /**
     * Construct new object SelectDictionaryForm*/
    public SelectDictionaryForm(Frame parent, String title, Dictionaries dictionaries, DefaultComboBoxModel<String> namesDictionariesCBM) {
        super(parent, title);
        this.dictionaries = dictionaries;
        this.namesDictionariesCBM =  namesDictionariesCBM;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        namesDictionaries = new JComboBox<>(namesDictionariesCBM);
        namesDictionaries.setPreferredSize(new Dimension(400,namesDictionaries.getPreferredSize().height));
        selectBtn = new JButton("Select");
        selectBtn.addActionListener(new SelectBtnListener());
        selectBtn.setMnemonic('S');

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelBtn.setMnemonic('C');

        mainPanel = new JPanel(new GridBagLayout());
        gridPanelBtn = new JPanel(new GridLayout(1,2,5,0));
        dictionaryLabel = new JLabel(" ");

        dictionaryLabel.setPreferredSize(new Dimension(300,dictionaryLabel.getPreferredSize().height)); //For right display status bar
        dictionaryLabel.setText(dictionaries.getDictionary(dictionaries.getCurrentIdDictionary()).getName());

        gridPanelBtn.add(selectBtn);
        gridPanelBtn.add(cancelBtn);

        mainPanel.add(namesDictionaries, new GridBagConstraints(0,0,1,1,1,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
        mainPanel.add(gridPanelBtn, new GridBagConstraints(0,1,1,1,0,1, GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0));
        mainPanel.setBorder(BorderFactory.createTitledBorder(""));

        statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
        statusBar.add(new JLabel(" Current dictionary: "));
        statusBar.add(dictionaryLabel);


        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                SelectDictionaryForm.this.namesDictionariesCBM.setSelectedItem(SelectDictionaryForm.this.dictionaries.getCurrentDictionary().getName());
            }
        });

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pack();
        //setSize(500,124);
        setLocationRelativeTo(parent);
        setVisible(true);

    }

    /**
     * Class <>SelectBtnListener</> implements listener for Select button*/
    class SelectBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            dictionaries.setCurrentIdDictionary(namesDictionaries.getSelectedIndex());
            dictionaryLabel.setText(dictionaries.getDictionary(dictionaries.getCurrentIdDictionary()).getName());
            namesDictionariesCBM.setSelectedItem(dictionaries.getCurrentDictionary().getName());
        }
    }
}
