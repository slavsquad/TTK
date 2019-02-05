package com.slavsquad.TTK.Forms;

import com.slavsquad.TTK.Text.Dictionaries.Dictionaries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class <>RemoveDictionaryForm</> implements GUI adn functional for Remove dictionaryLabel form
 */
public class RemoveDictionaryForm extends JDialog {

    Dictionaries dictionaries;
    JComboBox<String> namesDictionaries;
    JButton removeBtn, cancelBtn;
    JPanel mainPanel,gridPanelBtn;
    DefaultComboBoxModel<String> namesDictionariesCBM;


    /**
     * Construct new object of RemoveDictionaryForm*/
    public RemoveDictionaryForm(JFrame parent, String title, Dictionaries dictionaries,DefaultComboBoxModel<String> namesDictionariesCBM) throws HeadlessException {
        super(parent, title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.dictionaries = dictionaries;
        this.namesDictionariesCBM = namesDictionariesCBM;
        namesDictionaries = new JComboBox<>(namesDictionariesCBM);
        namesDictionaries.setPreferredSize(new Dimension(300, namesDictionaries.getPreferredSize().height));
        removeBtn = new JButton("Remove");
        removeBtn.addActionListener(new RemoveBtnListener());
        removeBtn.setMnemonic('R');
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        cancelBtn.setMnemonic('C');
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        gridPanelBtn = new JPanel(new GridLayout(1,2,5,0));

        gridPanelBtn.add(removeBtn);
        gridPanelBtn.add(cancelBtn);


        mainPanel.add(namesDictionaries, new GridBagConstraints(0,0,1,1,1,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
        mainPanel.add(gridPanelBtn, new GridBagConstraints(0,1,1,1,0,1, GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0));

        add(mainPanel, BorderLayout.CENTER);


        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                RemoveDictionaryForm.this.namesDictionariesCBM.setSelectedItem(RemoveDictionaryForm.this.dictionaries.getCurrentDictionary().getName());
            }
        });

        //setSize(250,104);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);


    }


    /**
     * Class <>RemoveBtnListener</> implements listener for Remove button*/
    class RemoveBtnListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(namesDictionariesCBM.getSize()>1) {
                int index = namesDictionaries.getSelectedIndex();
                dictionaries.getListDictionaries().remove(index);
                namesDictionariesCBM.removeElementAt(index);
                int sizeList = dictionaries.getListDictionaries().size();
                if (dictionaries.getCurrentIdDictionary() >= sizeList) {
                    dictionaries.setCurrentIdDictionary(sizeList - 1);
                    namesDictionariesCBM.setSelectedItem(dictionaries.getCurrentDictionary().getName());
                }
            } else{
                JOptionPane.showMessageDialog(RemoveDictionaryForm.this,"You can't remove all dictionaries!","Error!",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

}
