package com.slavsquad.TTK.Forms;

import com.slavsquad.TTK.Text.*;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Clas <>MainForm</> implements user graphics interface of application.
 *
 */
public class MainForm extends JFrame {
    JTextField  inputText;
    JLabel dictionaryLabel, idTextLabel, speedOfTyping, errorsCounterLabel,maxSpeedLabel,averageSpeedLabel, errorPercentlabel;
    File path = new File("dictionaries.out");
    JMenuBar menuBar;
    Dictionaries dictionaries;
    JFileChooser fileChooserOpen = new JFileChooser();
    JTextArea sourceText;
    WorkWithText workWithText;
    TimerLabel timerLabel;
    JPanel statusBar, cardPanel, mainPanel, resultPanel,buttonPanel, sessionPanel;
    JEditorPane resultText;
    JButton nextBtn,againBtn;
    String str;
    int sizeText = 500;
    DefaultComboBoxModel<String> nameDictionaresCBM;
    final static String MAINPANEL = "Card when user typing text";
    final static String RESULTPANEL = "Card when displays result and errors";
    final static double version = 1.0;


    /**
     * Construct object of MainForm and initialize needed field*/
    public MainForm() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Typing Tutor Keyboard");
        ImageIcon img = new ImageIcon(getClass().getResource("/images/icon.png"));
        setIconImage(img.getImage());
        /**Menu bar*/
        menuBar = new JMenuBar(){//Override this method in order to paint MenuBar like JLabel color
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(238,238,238));
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

            }
        };
        menuBar.add(createTypingMenu());
        menuBar.add(createDictionaryMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createStatisticsMenu());
        menuBar.add(createHelpMenu());
        setJMenuBar(menuBar);
        /**Source text*/
        str = " ";


        /**Card panel*/
        /**Card panel tab 1*/
        inputText = new JTextField();
        inputText.setMaximumSize(new Dimension(inputText.getMaximumSize().width,inputText.getPreferredSize().height));//Set normal width for JTextField
        inputText.setFont(new Font("Verdana", Font.PLAIN, 18));
        inputText.addKeyListener(new InputTextKeyListener());//Добавляем слушателя к елементу ввода строки пользователем

        sourceText = new JTextArea();
        sourceText.setFont(new Font("Verdana", Font.PLAIN, 18));
        sourceText.setLineWrap(true);
        sourceText.setWrapStyleWord(true);
        sourceText.setEditable(false);
        sourceText.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.add(inputText);
        mainPanel.add(new JScrollPane(sourceText));



        /**Card panel tab 2*/
        resultText = new JEditorPane();
        resultText.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        resultText.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        resultText.setFont(new Font("Verdana", Font.PLAIN, 18));
        resultText.setEditable(false);
        resultText.addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                resultText.setText(workWithText.showTextStatistics(true));
                resultText.setCaretPosition(0);
                nextBtn.requestFocus();
            }
        });


        nextBtn = new JButton("next Alt →");
        nextBtn.setMnemonic(KeyEvent.VK_RIGHT);
      //  nextBtn.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_MASK));
        nextBtn.addActionListener(new NextButtonListener());

        againBtn = new JButton("← Alt again");
        againBtn.setMnemonic(KeyEvent.VK_LEFT);
        againBtn.addActionListener(new AgainButtonListener());

        buttonPanel = new JPanel(new GridLayout(1,2,5,0));
        buttonPanel.add(againBtn);
        buttonPanel.add(nextBtn);

        resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        resultPanel.add(new JScrollPane(resultText), new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        resultPanel.add(buttonPanel, new GridBagConstraints(0,1,1,1,0,0, GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(5,0,1,0),0,0));

        cardPanel =  new JPanel(new CardLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder(""));
        cardPanel.add(mainPanel,MAINPANEL);
        cardPanel.add(resultPanel,RESULTPANEL);


        /**Status bar and its elements*/

        dictionaryLabel = new JLabel(" ");

        dictionaryLabel.setMaximumSize(new Dimension(100,dictionaryLabel.getPreferredSize().height));
        dictionaryLabel.setMinimumSize(new Dimension(0,dictionaryLabel.getPreferredSize().height));

        //
        dictionaryLabel.setFont(new Font("Dialog",Font.PLAIN,12));

        idTextLabel = new JLabel(" ");
        idTextLabel.setFont(new Font("Dialog",Font.PLAIN,12));
        //

        maxSpeedLabel = new JLabel("0");
        maxSpeedLabel.setFont(new Font("Dialog",Font.PLAIN,12));
        setFixSize(maxSpeedLabel,22);

        averageSpeedLabel = new JLabel("0");
        averageSpeedLabel.setFont(new Font("Dialog",Font.PLAIN,12));
        setFixSize(averageSpeedLabel,22);

        errorPercentlabel = new JLabel("0");
        errorPercentlabel.setFont(new Font("Dialog",Font.PLAIN,12));
        setFixSize(errorPercentlabel,28);


        statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
        int width = 3;

        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(new JLabel("Dictionary:"));
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(dictionaryLabel);
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(new JLabel("Text:"));
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(idTextLabel);
        statusBar.add(Box.createHorizontalStrut(width+50));

        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(new JLabel("Max speed:"));
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(maxSpeedLabel);
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(new JLabel("Average speed:"));
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(averageSpeedLabel);
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(new JLabel("Error:"));
        statusBar.add(Box.createHorizontalStrut(width));
        statusBar.add(errorPercentlabel);
        statusBar.add(Box.createHorizontalStrut(width));


        /**Session panel */
        timerLabel = new TimerLabel();
        timerLabel.setText("00:00");
        timerLabel.setFont(new Font("Dialog",Font.PLAIN,12));
        timerLabel.addPropertyChangeListener(new TimerLabelChangeListener());

        errorsCounterLabel = new JLabel("0");
        errorsCounterLabel.setFont(new Font("Dialog",Font.PLAIN,12));
        setFixSize(errorsCounterLabel,15);

        speedOfTyping = new JLabel("0");
        speedOfTyping.setFont(new Font("Dialog",Font.PLAIN,12));
        setFixSize(speedOfTyping,25);

        sessionPanel = new JPanel();
        sessionPanel.setLayout(new BoxLayout(sessionPanel,BoxLayout.X_AXIS));


        sessionPanel.add(Box.createHorizontalStrut(width));
        sessionPanel.add(new JLabel("Time:"));
        sessionPanel.add(Box.createHorizontalStrut(width));
        sessionPanel.add(timerLabel);
        sessionPanel.add(Box.createHorizontalStrut(width+7));
        sessionPanel.add(new JLabel("Errors:"));
        sessionPanel.add(Box.createHorizontalStrut(width));
        sessionPanel.add(errorsCounterLabel);
        sessionPanel.add(new JLabel("Speed:"));
        sessionPanel.add(Box.createHorizontalStrut(width));
        sessionPanel.add(speedOfTyping);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(sessionPanel);
        sessionPanel.setVisible(false);


        /**
         * Deserialize object dictionaryLabel*/
        deserializeFromFile();
        halt();


        /**Frame settings*/
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        addWindowListener(new WindowCloseListener());

        setSize(640, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Method start new round typing text*/
    private void start(){
        if(dictionaries!=null){
            String sourceTxt = dictionaries.getCurrentDictionary().getCurrentText().getText();
            inputText.setEnabled(true);
            sourceText.setEnabled(true);
            inputText.setText("");
            try{
                sourceText.setText(sourceTxt);
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null,"Text error: "+ex.getMessage());
            }
            sourceText.setCaretPosition(0);//JScrollPane always up
            workWithText = new WorkWithText(inputText,sourceText,timerLabel);
            CardLayout cl = (CardLayout)(cardPanel.getLayout());
            cl.show(cardPanel, MAINPANEL);
            inputText.requestFocus();
            resetIndicators();
            sessionPanel.setVisible(true);
        }
    }

    /**Method stops typing and specifies user how to start typing and inform him about current dictionaryLabel*/
    public void halt(){
        if (dictionaries!=null){
            if (!inputText.isVisible()){
                inputText.setVisible(true);
                statusBar.setVisible(true);
            }
            inputText.setEnabled(false);
            inputText.setText("");
            sourceText.setText("Please, select Typing -> Start for beginning of typing.\n");
            sourceText.append("Current dictionary: "+dictionaries.getCurrentDictionary().getName());
            sourceText.setEnabled(false);
            resetIndicators();
            sessionPanel.setVisible(false);
            workWithText=null;
            menuBar.getMenu(0).getItem(1).setVisible(false);
            menuBar.getMenu(0).getItem(2).setVisible(true);

            CardLayout cl = (CardLayout)(cardPanel.getLayout());
            cl.show(cardPanel, MAINPANEL);
        }
    }

    /**Method pauses typing of text*/
    public void pause(){
        if(workWithText!=null){
            workWithText.getTimer().pause();
            menuBar.getMenu(0).getItem(1).setVisible(true);
            menuBar.getMenu(0).getItem(2).setVisible(false);
            inputText.setEnabled(false);
            sourceText.setEnabled(false);
        }
    }

    /**Method proceeds typing of text*/
    public void proceed(){
        if(workWithText!=null){
            menuBar.getMenu(0).getItem(1).setVisible(false);
            menuBar.getMenu(0).getItem(2).setVisible(true);
            workWithText.getTimer().proceed();
            inputText.setEnabled(true);
            sourceText.setEnabled(true);
            inputText.requestFocus();
        }
    }

    /**Method reset status bar, session panel and set actual indicators*/
    public void resetIndicators(){
        timerLabel.setText("00:00");
        errorsCounterLabel.setText("0");
        speedOfTyping.setText("0");
        dictionaryLabel.setText(dictionaries.getCurrentDictionary().getName());
        dictionaryLabel.setMaximumSize(new Dimension(dictionaryLabel.getPreferredSize().width,dictionaryLabel.getPreferredSize().height)); //For right display status bar
        idTextLabel.setText(String.valueOf(dictionaries.getCurrentDictionary().getIdCurrentText()));
        maxSpeedLabel.setText(String.valueOf(dictionaries.getCurrentDictionary().getMaxSpeed()));
        averageSpeedLabel.setText(String.valueOf(dictionaries.getCurrentDictionary().getAverageSpeed()));
        errorPercentlabel.setText(String.valueOf(dictionaries.getCurrentDictionary().getErrorPercent())+"%");
    }

    /**
     * Class <>InputTexKeyListener</> implements listener for registration typing keys*/
    class InputTextKeyListener extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            int lastCountErrors = Integer.parseInt(errorsCounterLabel.getText());
            int newCountErrors =  workWithText.check();

            if (newCountErrors != lastCountErrors)
                errorsCounterLabel.setText(Integer.toString(newCountErrors));

            if(workWithText.isDone()){
                resultText.setText(workWithText.showTextStatistics(false));

                resultText.setCaretPosition(0);
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel,RESULTPANEL);
                sessionPanel.setVisible(false);
                nextBtn.requestFocus();
            }
        }
    }

    /**
     * Class <>NextButtonListener</> implements listener for button "Next"*/
    class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            /**
             * Grab and saving statistics
             */
            int speed = workWithText.getSpeedTyping();
            double errorPercent = workWithText.getErrorPercent();
            dictionaries.getCurrentDictionary().setAverageSpeed(speed);
            dictionaries.getCurrentDictionary().setMaxSpeed(speed);
            dictionaries.getCurrentDictionary().setErrorPercent(errorPercent);
            Text text = dictionaries.getCurrentDictionary().getCurrentText();
            text.setTime(workWithText.getTimer().getSeconds()*1000);
            text.setSpeed(speed);
            text.setError(workWithText.getErrorPercent());
            text.setListErrors(workWithText.getSortListErrors());
            text.setDate(new Date());

            if ((dictionaries.getCurrentDictionary().getIdCurrentText()+1)<dictionaries.getCurrentDictionary().getTextList().size()){
                dictionaries.getCurrentDictionary().nextIdText();
                start();
            }else {
                if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(MainForm.this,"You are already typed all texts in this dictionary! \n Do you want typing the dictionary again?","Confirm dialog",JOptionPane.YES_NO_OPTION)){
                    dictionaries.getCurrentDictionary().setIdCurrentText(0);
                    start();
                }
            }
        }
    }

    /**
     * Class <>AgainButtonListener</> implements listener for button "Again"*/
    class AgainButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            start();
        }
    }

    /**
     * Class <>TimerLabelChangeListener</> implements listener when TimerLabel's text change*/
    class TimerLabelChangeListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(workWithText==null){
                speedOfTyping.setText("0");
            }else{
                speedOfTyping.setText(String.valueOf(workWithText.speedKeyType()));
            }

        }
    }

    /**
     * Method creates menu Typing
     * @return typing - JMenu component, which contains menu items*/
    private JMenu createTypingMenu(){
        JMenu typing = new JMenu("Typing");
        typing.setMnemonic('T');

        JMenuItem start =  new JMenuItem("Start");
        start.setMnemonic('S');
        start.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
        start.addActionListener(new StartTypingListener());

        JMenuItem cont= new JMenuItem("Continue");
        cont.setMnemonic('C');
        cont.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK));
        cont.addActionListener(new ContinueTypingListener());
        cont.setVisible(false);

        JMenuItem pause = new JMenuItem("Pause");
        pause.setMnemonic('P');
        pause.setAccelerator(KeyStroke.getKeyStroke('P', KeyEvent.CTRL_MASK));
        pause.addActionListener(new PauseTypingListener());

        JMenuItem stop = new JMenuItem("Halt");
        stop.setMnemonic('H');
        stop.setAccelerator(KeyStroke.getKeyStroke('H', KeyEvent.CTRL_MASK));
        stop.addActionListener(new HaltTypingListener());

        typing.add(start);
        typing.add(cont);
        typing.add(pause);
        typing.add(stop);

        return typing;
    }

    /**
     * Method creates menu Dictionary
     * @return dictionaryLabel - JMenu component,which contains menu items*/
    private JMenu createDictionaryMenu(){
        JMenu dictionary = new JMenu("Dictionary");
        dictionary.setMnemonic('D');

        JMenuItem create =  new JMenuItem("New");
        create.setMnemonic('N');
        create.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_MASK));
        create.addActionListener(new NewDictionaryListener());

        JMenuItem select = new JMenuItem("Select");
        select.setMnemonic('l');
        select.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_MASK));
        select.addActionListener(new SelectDictionaryListener());

        JMenuItem edit = new JMenuItem("Edit");
        edit.setMnemonic('E');
        edit.setAccelerator(KeyStroke.getKeyStroke('E', KeyEvent.CTRL_MASK));
        edit.addActionListener(new EditDictionaryListener());

        JMenuItem delete = new JMenuItem("Remove");
        delete.setMnemonic('R');
        delete.setAccelerator(KeyStroke.getKeyStroke('R', KeyEvent.CTRL_MASK));
        delete.addActionListener(new RemoveDictionaryListener());

        dictionary.add(create);
        dictionary.add(select);
        dictionary.add(edit);
        dictionary.add(delete);

        return dictionary;
    }

    /**
     * Method creates menu View
     * @return View - JMenu component,which contains menu items*/
    private JMenu createViewMenu(){
        JMenu view = new JMenu("View");
        view.setMnemonic('V');

        JMenuItem font =  new JMenuItem("Font");
        font.setMnemonic('F');
        font.setAccelerator(KeyStroke.getKeyStroke('F', KeyEvent.CTRL_MASK));
        font.addActionListener(new FontViewListener());

        view.add(font);
        return view;
    }

    /**
     * Method creates menu Statistics
     * @return - JMenu component, which contains menu items
     */
    public JMenu  createStatisticsMenu(){
        JMenu statistics = new JMenu("Statistics");
        statistics.setMnemonic('A');

        JMenuItem display =  new JMenuItem("Display");
        display.setMnemonic('D');
        display.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_MASK));
        display.addActionListener(new DisplayStatisticsListener());

        statistics.add(display);
        return statistics;
    }

    /**
     * Method creates menu Help
     * @return - JMenu component, which contains menu items
     */
    public JMenu  createHelpMenu(){
        JMenu statistics = new JMenu("Help");
        statistics.setMnemonic('H');

        JMenuItem display =  new JMenuItem("About");
        display.setMnemonic('o');
        display.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));
        display.addActionListener(new AboutHelpListener());

        statistics.add(display);
        return statistics;
    }


    /**
     * Method set fixed size pointed component
     * @param element - component which needed set fixed size
     * @param size - specified size
     */
    public static void setFixSize(JComponent element,int size){
        Dimension dimension = element.getPreferredSize();
        element.setMinimumSize(new Dimension(dimension.width+size,dimension.height));
        element.setPreferredSize(new Dimension(dimension.width+size,dimension.height));
        element.setMaximumSize(new Dimension(dimension.width+size,dimension.height));
    }

    /**
     * Class <>StartTypingListener</> implements listener for menu item, Typing->Start*/
    class StartTypingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            start();
        }

    }

    /**
     * Class <>PauseTypingListener</> implements listener for menu item, Typing->Pause*/
    class PauseTypingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pause();
        }
    }

    /**
     * Class <>ContinueTypingListener</> implements listener for menu item, Typing->Continue*/
    class ContinueTypingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            proceed();
        }
    }

    /**
     * Class <>HaltTypingListener</> implements listener for menu item, Typing->Halt*/
    class HaltTypingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            halt();
        }
    }

    /**
     * Class<>TextSizeSlider</> implements slider for setting size of text
     */
    class TextSizeSlider extends JPanel{
        /**
         * Construct new instance of TextSizeSlider
         */
        public TextSizeSlider(){
            JSlider slider = new JSlider(0, 999, sizeText);
            slider.setOrientation(JSlider.VERTICAL);
            slider.setMajorTickSpacing(250);
            slider.setMinorTickSpacing(50);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            final JTextField sizeOfText = new JTextField(String.valueOf(sizeText));
            sizeOfText.setEditable(false);
            sizeOfText.setBorder(null);

            slider.addChangeListener(e -> {
                // меняем надпись
                int value = ((JSlider)e.getSource()).getValue();
                sizeOfText.setText(String.valueOf(value));
                sizeText = value;
            });

            setLayout(new GridBagLayout());
            add(new JLabel("Text size:"),new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE, new Insets(0,5,0,0), 0, 0));
            add(slider,new GridBagConstraints(0,1,1,1,0,1,GridBagConstraints.NORTH,GridBagConstraints.VERTICAL, new Insets(0,5,0,0), 0, 0));
            add(sizeOfText,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));
        }
    }

    /**
     * Class <>NewDictionaryListener</> implements listener for menu item, Dictionary -> New*/
    class NewDictionaryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooserOpen.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            fileChooserOpen.removeChoosableFileFilter(fileChooserOpen.getAcceptAllFileFilter());

            fileChooserOpen.setAccessory(new TextSizeSlider());
            int ret = fileChooserOpen.showDialog(MainForm.this, "Open");
            if (ret == JFileChooser.APPROVE_OPTION) {
                try {
                    final File file = new File(fileChooserOpen.getSelectedFile().toString());
                    String fileName = Info.getFileNameWithoutExtention(file.getName());

                    boolean containsDictionary = false;
                    if (dictionaries!=null){
                        containsDictionary = dictionaries.getNames().contains(fileName);
                    }
                    if (containsDictionary){
                        JOptionPane.showMessageDialog(MainForm.this,"Dictionary "+fileName+" already exist!","Error!",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        try {
                            if (dictionaries==null){
                                dictionaries = new Dictionaries();
                                nameDictionaresCBM = new DefaultComboBoxModel<>();
                            }
                            new ConversionProgressForm(MainForm.this,"Conversion",dictionaries,file, Info.getCountCharInFile(file),sizeText);

                            nameDictionaresCBM.addElement(fileName);
                            nameDictionaresCBM.setSelectedItem(dictionaries.getCurrentDictionary().getName());
                            halt();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }


    /**
     * Class <>EditDictionaryListener</> implements listener for item menu Dictionary->Edit */
    class EditDictionaryListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (dictionaries!=null){
                halt();
                new EditDictionaryForm(MainForm.this,"Edit dictionary",dictionaries,nameDictionaresCBM);
            }


        }
    }

    /**
     * Class <>RemoveDictionaryListener</> implements listener for item menu Dictionary->Remove */
    class RemoveDictionaryListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (dictionaries!=null){
                pause();
                new RemoveDictionaryForm(MainForm.this,"Remove dictionary",dictionaries,nameDictionaresCBM);
                halt();
            }
        }
    }

    /**
     * Class <>SelectDictionaryListener</> implements listener for item menu Dictionary->Select */
    class SelectDictionaryListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (dictionaries!=null){
                pause();
                new SelectDictionaryForm(MainForm.this,"Select dictionary",dictionaries,nameDictionaresCBM);
                halt();
            }
        }
    }

    /**
     * Class <>FontViewListener</> implements listener for item menu View->Font */
    class FontViewListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            pause();
            JFontChooser fontChooser = new JFontChooser();
            fontChooser.setSelectedFont(new Font("Verdana", Font.PLAIN, 18));
            fontChooser.showDialog(MainForm.this);
            Font selectFont = fontChooser.getSelectedFont();
            inputText.setFont(selectFont);
            sourceText.setFont(selectFont);
            resultText.setFont(selectFont);
            proceed();
        }
    }

    /**
     * Class <>DisplayStatisticsListener</> implements listener for item menu Statistics -> Display
     */
    class DisplayStatisticsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (dictionaries!=null){
                pause();
                new StatisticsForm(MainForm.this,"Statistics",dictionaries);
            }
        }
    }

    /**
     * Class <>AboutHelpListener</> implements listener for item menu Help -> About
     */
    class AboutHelpListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (dictionaries!=null){
                pause();
                JOptionPane.showOptionDialog(MainForm.this,createAboutMessageComponenets(),
                        "About", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
            }
        }
    }

    /**
     * Methos creates hyper link
     * @param text displayed in hyper link
     * @param url refered hyper link
     * @param toolTip displayed under hyper link
     * @return hyper link in form Jlabel
     */
    public static JLabel createHyperLink(final String text, String url, final String toolTip)
    {
        URI temp = null;
        try{
            temp = new URI(url);
        }catch (URISyntaxException ex){
            JOptionPane.showConfirmDialog(null,"Wrong syntaxis of link!");
        }

        final URI uri = temp;
        final JLabel link = new JLabel();
        link.setText("<HTML><FONT color=\"#000099\">"+text+"</FONT></HTML>");
        if(!toolTip.equals(""))
            link.setToolTipText(toolTip);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter()
        {
            public void mouseExited(MouseEvent arg0)
            {
                link.setText("<HTML><FONT color=\"#000099\">"+text+"</FONT></HTML>");
            }

            public void mouseEntered(MouseEvent arg0)
            {
                link.setText("<HTML><FONT color=\"#000099\"><U>"+text+"</U></FONT></HTML>");
            }

            public void mouseClicked(MouseEvent arg0)
            {
                if (Desktop.isDesktopSupported()) {
                    try {
                        if (uri!=null){
                            Desktop.getDesktop().browse(uri);
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,"Wrong URL!");
                    }
                } else {
                    JTextField adress = new JTextField(toolTip);
                    adress.setBackground(null);
                    adress.setEditable(false);
                    adress.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                    adress.setComponentPopupMenu(createPopupMenu(adress));
                    JPanel messagePanel = new JPanel(new GridBagLayout());

                    messagePanel.add(new JLabel("Your system doesn't support this action!"), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE, new Insets(5,0,0,0), 0, 0));
                    messagePanel.add(new JLabel("Copy this link in browser and open: "), new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE, new Insets(0,0,5,0), 0, 0));
                    messagePanel.add(adress, new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE, new Insets(0,0,5,0), 0, 0));

                    JOptionPane.showOptionDialog(null, messagePanel,
                            "Warring!", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
                }
            }

            private JPopupMenu createPopupMenu(final JTextComponent component) {
                JPopupMenu pm = new JPopupMenu();
                JMenuItem copy = new JMenuItem("Copy");
                copy.addActionListener(e -> component.copy());
                pm.add(copy);
                return pm;
            }
        });
        return link;
    }

    /**
     * Method creates components for messages "About"
     * @return array of component which contains in message
     */
    public JPanel createAboutMessageComponenets(){
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        JPanel componentPanel = new JPanel(new GridBagLayout());
        infoPanel.add(new JLabel("<html>&copy;&nbsp;</html>"));
        infoPanel.add(createHyperLink("WTFPL","http://www.wtfpl.net/about/","www.wtfpl.net/about/"));
        infoPanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&#60;\\&#62;&nbsp;</html>"));
        infoPanel.add(createHyperLink("github","https://github.com/slavsquad/TypistTrainerKeyboard.git","github.com/slavsquad/TypistTrainerKeyboard.git"));
        infoPanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;@&nbsp;</html>"));
        infoPanel.add(createHyperLink("slavsquad","mailto:slavsquad@yandex.com","slavsquad@yandex.com"));
        componentPanel.add(new JLabel("<html><font size = \"5\">Typing Tutor Keyboard</font></html>"), new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
        componentPanel.add(new JLabel("<html><font size = \"2\">ver. "+version+"</font></html>"), new GridBagConstraints(0,1,1,1,0,1,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
        componentPanel.add(new JLabel("<html>The cross platform typing trainer.<br/>MainForm has some features:"+
                "<br/> &nbsp;&nbsp;&nbsp;&#8226; typing any texts on time;"+
                "<br/> &nbsp;&nbsp;&nbsp;&#8226; add and remove dictionary;" +
                "<br/> &nbsp;&nbsp;&nbsp;&#8226; edit text of dictionary;" +
                "<br/> &nbsp;&nbsp;&nbsp;&#8226; monitoring statistics.</p></html>"),new GridBagConstraints(0,2,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
        componentPanel.add(infoPanel, new GridBagConstraints(0,3,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(12,0,0,0),0,0));

        return componentPanel;
    }

    /**
     * Class <>WindowCloseListener</> implements listener when closing the main window*/
    class WindowCloseListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e){
            serializeInFile();
        }
    }

    /**
     * Method serializes data of dictionaries in file dictionaries.out
     */
    public void serializeInFile()
    {
        try {
            if (dictionaries!=null){
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(dictionaries);
                oos.flush();
                oos.close();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    /**
     * Method deserialize data of dictionaries from file dictionaries.out
     */
    public void deserializeFromFile(){
        if (path.exists()) {
            try{
                FileInputStream fis = new FileInputStream(path);
                ObjectInputStream oin = new ObjectInputStream(fis);
                dictionaries = (Dictionaries) oin.readObject();
                oin.close();

                /**Fill comboBox model*/
                nameDictionaresCBM = new DefaultComboBoxModel<>(dictionaries.getNames());
                nameDictionaresCBM.setSelectedItem(dictionaries.getCurrentDictionary().getName());

                dictionaryLabel.setText(dictionaries.getCurrentDictionary().getName());
                idTextLabel.setText(String.valueOf(dictionaries.getCurrentDictionary().getIdCurrentText()));

            }catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            dictionaries = null;
            nameDictionaresCBM = null;
            statusBar.setVisible(false);
            inputText.setVisible(false);
            sourceText.setText("File dictionaries.out does not found. Please, select menu Dictionary -> New, and open new dictionary file.");
        }
    }
}