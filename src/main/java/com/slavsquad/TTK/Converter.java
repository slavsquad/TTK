package com.slavsquad.TTK;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class <>Converter</> implements conversion txt files to Dictionary object
 */

public class Converter extends SwingWorker<List<Text>,Integer>{


    BoundedRangeModel progressBarBRM;
    List<Text> listOfTexts;
    Dictionaries dictionaries;
    File file;
    int sizeText = 0;

    public Converter(File file,Dictionaries dictionaries,int sizeText, BoundedRangeModel progressBarBRM){
        this.file = file;
        this.dictionaries = dictionaries;
        this.sizeText = sizeText;
        this.progressBarBRM = progressBarBRM;
    }

    /**
     * Method splits txt file on text, sizeText characters in each
     * @return listOfTexts - ArrayList when contain texts*/
    @Override
    protected List<Text> doInBackground() throws Exception {
        listOfTexts = new ArrayList<>();

        int numChar,countChar;
        StringBuilder text = new StringBuilder();
        try {//Open thread for reading file
            InputStreamReader reader =new InputStreamReader(new FileInputStream(file),"windows-1251");
            int read,temp;
            countChar = 0;
            numChar = 0;
            while ((read=reader.read())!=-1){//if reading character will be -1 then file is ended
                int lastChar = 0;
                temp = clearServiceChar(read);//Clear text from services chars
                if (text.length() != 0){
                    lastChar = (int)text.charAt(text.length()-1);
                }

                if (numChar> sizeText &&(lastChar==33||lastChar==63||lastChar==46)&&temp==32){
                    listOfTexts.add(new Text(clearText(text.toString())));
                    text.delete(0,numChar);
                    numChar=0;
                }
                char ch = (char)(temp);
                text.append(ch);
                numChar++;
                countChar++;
                publish(countChar);
            }

//            Write last text if it not null
            if (clearText(text.toString()).length()!=0){
                listOfTexts.add(new Text(clearText(text.toString())));
            }

            //Close reading thread
            reader.close();
        }catch (IOException ex){
           JOptionPane.showMessageDialog(null,"Error text: " + ex.getMessage());
        }

        return listOfTexts;
    }

    /**
     * Method clears compleated text from spaces and other spec symbols
     * @param text - compleated text
     * @return - cleared compeated text
     */
    protected String clearText(String text){
        return text.trim().replaceAll("[\\s]{2,}", " ").replace("\u0085","...");
    }

    @Override
    public void process(List<Integer> chunks) {
        // increase current value progress bar
        for (Integer percent:chunks){
            progressBarBRM.setValue(percent);
        }
    }

    @Override
    protected void done(){
        String fileName = Info.getFileNameWithoutExtention(file.getName());
        if (dictionaries==null){dictionaries = new Dictionaries();}
        dictionaries.add(new Dictionaree(fileName,listOfTexts));
        dictionaries.setCurrentIdDictionary(dictionaries.getListDictionaries().size()-1);
    }

    /**
     * Method replace spec and service character on typical symbol
     * @param character - the symbol which needed to check
     * @return character already fixed*/
    private int clearServiceChar(int character){
        switch (character) {
            case -1:
                character = 32;
                break;
            case 0:          //Insert space instead of wrapping of text, horizontal and vertical tabulation, null symbol and etc.
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 160:
                character = 32;
                break;
            case 132:        //Insert " instead of: „ “ ” « »
            case 147:
            case 148:
            case 171:
            case 187:
                character = 34;
                break;
            case 96:         //Insert ' instead of: ` ‘ ’
            case 145:
            case 146:
                character = 39;
                break;
            case 150:        //Insert - instead of: – —
            case 151:
            case 8211:
                character = 45;
                break;
            case 133:        //Insert ... instead of: …
            case 8230:
                character = 133;
                break;
        }
        return character;
    }
}
