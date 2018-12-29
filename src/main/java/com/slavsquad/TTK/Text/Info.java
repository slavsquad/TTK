package com.slavsquad.TTK.Text;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class <code>Info</code> implement methods which give information about source text.
 * @author  svalSquad
 */
public class Info {
    /**
     * Method calculates number of lines for JTextArea.
     * @param component JTextArea component contains source text
     * @return number of lines, including wrapped lines
     */
    static public int getWrappedLines(JTextArea component) {
        View view = component.getUI().getRootView(component).getView(0);
        int preferredHeight = (int) view.getPreferredSpan(View.Y_AXIS);
        int lineHeight = component.getFontMetrics(component.getFont()).getHeight();
        return preferredHeight / lineHeight;
    }

    /**
     * Method calculates minimum width of view for JTextArea
     * @param component JTextArea component contains text
     * @return minimum width of view
     */
    static public int getMinimumWidth(JTextArea component) {
        View view = component.getUI().getRootView(component).getView(0);//Get View for JTextArea
        return (int) view.getMinimumSpan(View.X_AXIS);//Get and return minimum width for View
    }

    /**
     * Method calculates width input's string
     * @param component JTextArea component which contains source text
     * @param str       the input string
     * @return width of string
     */
    static public int getWidthStr(JTextArea component, String str) {
        return component.getFontMetrics(component.getFont()).stringWidth(str);//Get and return width inputs's string, for this get font for specified component
    }

    /**
     * Method calculate length 1-st displayed string in source text area
     * @return length string in quantity characters
     */
    static public int getMaxStrLength(JTextArea component, int length) {

        StringBuilder strBld = new StringBuilder();
        int lengthStr = 0, i = 0;
        String str = component.getText();

        while (getWidthStr(component, strBld.toString()) < length) {
            if (i==str.length()) {
                lengthStr = str.length();
                break;
            }
            char ch = str.charAt(i);
            if(ch==32){
                lengthStr = strBld.length();
            }
            strBld.append(ch);
            i++;
        }
        return lengthStr;
    }

    /**
     * Method count quantity characters in file
     * @return quantity characters*/
    static public int getCountCharInFile(File file){
        int numChar = 0;
        try {
            InputStreamReader reader =new InputStreamReader(new FileInputStream(file),"windows-1251");
            while (reader.read()!=-1){
                numChar++;
            }
            reader.close();
        }catch (IOException ex){
            JOptionPane.showMessageDialog(null,"Text error: "+ex.getMessage());
        }
        return numChar;
    }

    /**
     * Method gets file name without extention
     * @return file name without extention
     */
    static public String getFileNameWithoutExtention(String fileName){
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
}
