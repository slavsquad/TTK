package com.slavsquad.TTK;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class <>Dictionaree</> implements container which storage list of texts
 */


public class Dictionaree implements Serializable{
    private String name;
    private int idCurrentText;
    private List<Text> textList = new ArrayList<>();
    private int maxSpeed;
    private int averageSpeed;
    private double errorPercent;

    /**
     * Construct new dictionaryLabel*/
    public Dictionaree(String name, List<Text> textList) {
        averageSpeed = 0;
        this.idCurrentText = 0;
        errorPercent = 0;
        this.maxSpeed = 0;
        this.name = name;
        this.textList = textList;
    }

    /**
     * Method gets name of dictionaryLabel
     * @return name - the string, name of dictionaryLabel*/
    public String getName() {
        return name;
    }

    /**
     * Method sets name of dictionaryLabel
     * @param name - the string, name of dictionaryLabel*/
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method gets id number of current text
     * @return idCurrentText -  id number current text which typing user*/
    public int getIdCurrentText() {
        return idCurrentText;
    }

    /**
     * Method sets id numver for current text
     * @param idCurrentText - the id number of text, which typing user*/
    public void setIdCurrentText(int idCurrentText) {
        this.idCurrentText = idCurrentText;
    }

    /**
     * Method gets list of texts
     * @return list of text, when store all texts*/
    public List<Text> getTextList() {
        return textList;
    }

    /**
     * Method set lisft of texts
     * @param textList - the listArray when store texts*/
    public void setTextList(List<Text> textList) {
        this.textList = textList;
    }
    /**
     * Method gets current text
     * @return Text's object which contains current text in a dictionary*/
    public  Text getCurrentText(){
        return textList.get(idCurrentText);
    }

    /**
     * Method increase idCurrentTxt by one*/
    public void nextIdText(){
        idCurrentText++;
    }

    /**
     * Method gets text accordance its id
     * @param id - id number of text
     * @return Text's object which contains text of typing*/
    public Text getText(int id){
        return textList.get(id);
    }

    /**
     * Method gets maximum speed of typing for current dictionary
     * @return maximum speed of typing char per seconds
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Method set maximum speed for current dictionary
     * if only old result max speed less new result
     * @param speed - speed of typing
     */
    public void setMaxSpeed(int speed) {
        if (maxSpeed<speed){
            maxSpeed = speed;
        }
    }

    /**
     * Method gets average speed of typing for current dictionary
     * @return average speed for current dictionary
     */
    public int getAverageSpeed() {
        return averageSpeed;
    }

    /**
     * Method calculates average speed of typing for current dictionary
     * @param speed - speed of typing
     */
    public void setAverageSpeed(int speed) {
        int coutText = idCurrentText + 1;
        averageSpeed = (averageSpeed*idCurrentText+speed)/coutText ;
    }

    /**
     * Method gets average errorPercent's percentage
     * @return average percent of errors
     */
    public double getErrorPercent() {
        return errorPercent;
    }

    /**
     * Method calculates average percentage of errors
     * @param percent of errors
     */
    public void setErrorPercent(double percent) {
        int coutText = idCurrentText + 1;
        double errPercent = (errorPercent *idCurrentText+percent)/coutText;
        errorPercent = BigDecimal.valueOf(errPercent).setScale(1,BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }
}
