package com.slavsquad.TTK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class <>EditDictionaryForm</> implements form for edit texts of dictionaryLabel*/
public class EditDictionaryForm  extends JDialog {


    JLabel dictionaryLabel,textLabel,capacityLabel, capacityLabelCount;
    JComboBox<String> nameDictionary;
    JTextField textId;
    JButton switchBtn, saveBtn, cancelBtn;
    JTextArea text;
    Dictionaries dictionaries;
    private int textSelectId;
    DefaultComboBoxModel<String> namesDictionariesCBM;
    SelectDictionaryListener selectDictionaryListener;


    /**
     * Construct form EditDictionaryForm*/
    public EditDictionaryForm(JFrame parent, String title, Dictionaries dictionaries, final DefaultComboBoxModel<String> namesDictionariesCMB) throws HeadlessException {

        super(parent,title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        textSelectId = 0;

        this.dictionaries = dictionaries;
        this.namesDictionariesCBM = namesDictionariesCMB;

        final JPanel mainPanel = new JPanel(new GridBagLayout());
        dictionaryLabel = new JLabel("Dictionary:");
        nameDictionary = new JComboBox<>(namesDictionariesCMB);

        selectDictionaryListener = new SelectDictionaryListener();
        nameDictionary.addActionListener(selectDictionaryListener);

        textLabel = new JLabel("Text number:");
        textLabel.setDisplayedMnemonic('T');
        textId = new JTextField("0",4);
        textId.addActionListener(new SwitchBtnListener());
        textLabel.setLabelFor(textId);
        switchBtn = new JButton("Switch");
        switchBtn.addActionListener(new SwitchBtnListener());
        switchBtn.setMnemonic('W');
        text = new JTextArea();
        text.setFont(new Font("Verdana", Font.PLAIN, 16));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(new SaveBtnListener());
        saveBtn.setMnemonic('S');
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        cancelBtn.setMnemonic('C');
        capacityLabel = new JLabel("Capacity:");
        capacityLabelCount = new JLabel("0");

        JPanel gridPnlBnt = new JPanel(new GridLayout(1,2,5,0));
        gridPnlBnt.add(saveBtn);
        gridPnlBnt.add(cancelBtn);

        JPanel flowPnlCapacity = new JPanel();
        flowPnlCapacity.add(capacityLabel);
        flowPnlCapacity.add(capacityLabelCount);

        JPanel gridPnlText = new JPanel(new GridLayout(1,2,5,0));
        gridPnlText.add(textId);
        gridPnlText.add(switchBtn);


        mainPanel.add(dictionaryLabel,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(5,5,0,0),0,0));
        mainPanel.add(nameDictionary, new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(5,0,0,5),0,0));
        mainPanel.add(flowPnlCapacity, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(5,0,0,0),0,0));
        mainPanel.add(textLabel, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
        mainPanel.add(gridPnlText, new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,0,5),0,0));
        mainPanel.add(new JScrollPane(text), new GridBagConstraints(0,2,3,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(5,5,0,5),0,0));
        mainPanel.add(gridPnlBnt, new GridBagConstraints(1,3,2,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(5,0,5,5),0,0));

        add(mainPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                EditDictionaryForm.this.namesDictionariesCBM.setSelectedItem(EditDictionaryForm.this.dictionaries.getCurrentDictionary().getName());
                nameDictionary.removeActionListener(EditDictionaryForm.this.selectDictionaryListener);
            }
        });


        displayText(nameDictionary.getSelectedIndex(),Integer.valueOf(textId.getText()));

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(640,480);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Method displays selected text*/
    public void displayText(int idDictionary, int idText){
        capacityLabelCount.setText(String.valueOf(dictionaries.getDictionary(nameDictionary.getSelectedIndex()).getTextList().size()));
        text.setText(dictionaries.getDictionary(idDictionary).getText(idText).getText());
        text.setCaretPosition(0);
    }


    /**
     * Class <>SaveBtnListener</> implements listener for Save button*/
    class SaveBtnListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            dictionaries.getDictionary(nameDictionary.getSelectedIndex()).getTextList().get(textSelectId).setText(text.getText());
        }
    }
    /**
     * Class <>SwitchBtnListener</> implements listener for Switch button*/
    class SwitchBtnListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                textSelectId = Integer.valueOf(textId.getText());
                displayText(nameDictionary.getSelectedIndex(),textSelectId);
            } catch (IndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(EditDictionaryForm.this,"Please, input text number from 0 to "+(Integer.valueOf(capacityLabelCount.getText())-1));
                clearTextField(textId);
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(EditDictionaryForm.this,"You input wrong number!");
                clearTextField(textId);
            }
        }

        /**
         * Method clear text field and request its focus
         */
        private void clearTextField(JTextField textField){
            textField.setText(null);
            textField.requestFocus();
        }
    }

    /**
     * Class <>SelectDictionaryListener</> implements listener comboBox*/
    class SelectDictionaryListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            textId.setText("0");
            textSelectId = 0;
            displayText(nameDictionary.getSelectedIndex(),textSelectId);
        }
    }
}
