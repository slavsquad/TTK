package com.slavsquad.TTK.Text;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class <code>WorkWithText</code> implement methods check text, find and save user's error.
 * Class have private field and public methods.
 * @author  svalSquad
 */
public class WorkWithText {
    private String  taskText;
    private int offset = 0;
    private JTextField inputText;
    private JTextArea sourceText;
    private List<TextError> listErrors = new ArrayList<>();
    boolean errorFlag = false;
    private TimerLabel timerLabel;
    private int numKeys;
    private boolean isDone = false;
    private Object highLight;

    /**
     * Construct new object WorkWithText
     * @param inputTxt store JTextField component, when user inputs text
     * @param sourceTxt store JTextArea component, when source text displays
     * */
    public WorkWithText(JTextField inputTxt,JTextArea sourceTxt) {
        this.inputText = inputTxt;
        this.sourceText = sourceTxt;
        this.taskText = sourceTxt.getText();
        numKeys = 0;

        try{
            highLight = sourceTxt.getHighlighter().addHighlight(0,0, new DefaultHighlighter.DefaultHighlightPainter(new Color(0xc8d6e6)));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**Overload constructor construct new object like WorkWthText plus timer
     * @param inputTxt store JTextField component, when user inputs text
     * @param sourceTxt store JTextArea component, when source text displays
     * @param timer store timerLaber object, when realised timer mechanism.
     * */
    public WorkWithText(JTextField inputTxt, JTextArea sourceTxt, TimerLabel timer){
        this(inputTxt,sourceTxt);
        timerLabel=timer;
        timer.restart();
    }

    /**
     * Method compares input string and source string, find error and record its in the listArray
     * @param inputStr the input user's string
     * @param sourceSrt the string of source text
    * */
    public void findAndSaveError(String inputStr,String sourceSrt){
        sourceText.setForeground(Color.red);
        errorFlag=true;

        char[] inputCharArr = inputStr.toCharArray();
        char[] sourceCharArr = sourceSrt.toCharArray();
        int errPosition;
        for (int i=0;i<inputCharArr.length;i++){
            if (inputCharArr[i]!=sourceCharArr[i]){
                errPosition=i+offset;
                listErrors.add(new TextError(errPosition,inputCharArr[i]));
                return;
            }
        }
    }

    /**
     *Method verifies input user's text and source text also displays errors to user when it needs
     * @return quantity user's errors
     */
    public int check(){
        numKeys++;
        try{
            if (inputText.getText().length()<=sourceText.getText().length()) {
                int inStrLen = inputText.getText().length();
                String sourceStr=sourceText.getText(0,inStrLen);

                if (inputText.getText().equals(sourceStr)) {
                    sourceText.setForeground(Color.black);//Paint text black color, because after error source test painted red color

                    int TextWidth = Info.getMinimumWidth(sourceText);
                    int maxStrLength = Info.getMaxStrLength(sourceText,TextWidth);


                   changeHighLight(highLight,0,inputText.getCaretPosition());

                    if(sourceStr.length()>maxStrLength){
                        String temp = sourceText.getText();
                        int strOffset = maxStrLength+1;
                        inputText.setText(sourceStr.substring(strOffset));
                        offset += strOffset;
                        sourceText.setText(temp.substring(strOffset));
                        sourceText.setCaretPosition(0);//JScrollPane always up
                    }

                    if (inputText.getText().length() == sourceText.getText().length()) {
                        isDone=true;
                        timerLabel.stop();
                    }

                    errorFlag = false;

                } else {
                    if(!errorFlag){
                        findAndSaveError(inputText.getText(),sourceStr);
                    }
                }
            }else{
                if(!errorFlag){
                    findAndSaveError(inputText.getText(),sourceText.getText());
                }
            }
        } catch (Exception io){
            io.printStackTrace();
        }
        return listErrors.size();

    }
    /**
     * Method count speed of typing keys
     * @return quantity typing keys per minute*/
    public int speedKeyType(){
        int speed = numKeys*60;
        numKeys = 0;
        return speed;
    }

    /**
     * Method count speed of typing
     * @return quantity typed characters per minute*/
    public int getSpeedTyping(){
        double minute = (double)timerLabel.getSeconds()/60;
        double speed = (double)taskText.length()/minute;
        return (int)Math.round(speed);
    }

    /**
     * Method calculates percent of errors in a text
     * @return percentage or errors
     */
    public double getErrorPercent(){
        return  100/(double)taskText.length()*listErrors.size();
    }

    /**
     * Method inform when used finish typing text
     * @return field isDonw, true - text is finish typing, false - not yet*/
    public boolean isDone(){
        return isDone;
    }

    /**
     * Method sorts and gets list of errors
     * @return listErrors - ArrayList when contains sort's list of objects TextError*/
    public List<TextError> getSortListErrors(){

        Collections.sort(listErrors, (o1, o2) -> Integer.compare(o1.getPosition(),o2.getPosition()));

        return listErrors;
    }

    /**
     * Method gets text with errors which user made
     * @param showErrors - if this field true then displays user's errors
     * @return resultStr - the string contains text with user's errors, errors are displayed through html and css*/
    public String showTextStatistics(boolean showErrors){
        String originalText = taskText;
        StringBuilder resultStr = new StringBuilder("<html><style>u{font-weight: bold; color: red;}</style><u></u>" +
                "<p align=\"center\"><b>Speed: <u>"+ getSpeedTyping()+"</u></b> ch/min.&nbsp;&nbsp;<b>Time: <u>"+timerLabel.getText() +
                "</u> </b>min.<b>&nbsp;&nbsp;Errors: <u>"+String.format("%1$.2f",getErrorPercent())+"</u></b> %</p><br/>");
        if (showErrors){
            resultStr.append("<div align=\"center\"><font size=\"3\">Look at errors in the text:</font></div>");
            List<TextError> listErrors = getSortListErrors();
            int startIndex = 0;

            if (listErrors.size()==0){
                resultStr.append("<div align=\"center\"><br/>Congratulations! You have no errors!</div>");
            }else {
                for(TextError error:listErrors){
                    int position = error.getPosition();
                    resultStr.append(originalText.substring(startIndex,position));
                    resultStr.append("<u>");
                    resultStr.append(String.valueOf(error.getCharError()));
                    resultStr.append("</u>");
                    startIndex = position;

                }
                resultStr.append("<div>");
                resultStr.append(originalText.substring(startIndex,originalText.length()));
                resultStr.append("</div>");
            }
        }else {
            resultStr.append("<div align=\"center\"><font size=\"3\"><a href=>show errors</a></font></div>");
        }

        resultStr.append("</html>");
        return resultStr.toString();
    }

    /**
     * Method highlight text which user already typing
     * @param start - beginning of highlight text
     * @param end - finish of highlight text*/
    private void changeHighLight(Object reference, int start, int end){
        try{
            sourceText.getHighlighter().changeHighlight(reference,start,end);
        }catch (BadLocationException io){
            JOptionPane.showMessageDialog(new JFrame(), "Congratulation! You have a warnings: "+io);
        }
    }

    /**
     * Method gets timer
     * @return timer - TimerLabel oject*/
    public TimerLabel getTimer(){
        return timerLabel;
    }

}