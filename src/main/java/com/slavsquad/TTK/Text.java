package com.slavsquad.TTK;

/**
 * Created by stepanenko.sg on 26.04.2017.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class <>Text</> implements container which storage text of typing and statistic's fields
 */
public class Text implements Serializable {
    private int speed;
    private List<TextError> listErrors;
    private long time;     //time which user spent for typing text(milliseconds)
    private Date date;    //date and time when text was typed
    private String text;
    private double error;

    /**
     * Construct new text and statics fields
     * */
    public Text(String txt){
        speed = 0;
        listErrors = new ArrayList<>();
        time = 0;
        text = txt;
    }

    /**
     * Mehtod gets text's speed of typing
     * @return speed - characters per minute
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Method sets test's speed of typing
     * @param speed of typing  - characters per minute
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Method gets list of error for text
     * @return list of errors
     */
    public List<TextError> getListErrors() {
        return listErrors;
    }

    /**
     * Method sets list of error for text
     * @param listErrors - list of errors
     */
    public void setListErrors(List<TextError> listErrors) {
        this.listErrors = listErrors;
    }

    /**
     * Method gets time which spent user by typing text
     * @return time(seconds)
     */
    public long getTime() {
        return time;
    }

    /**
     * Method sets time which spent user by typing text
     * @param time - seconds
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Method gets text for typing
     * @return text of typing
     */
    public String getText() {
        return text;
    }

    /**
     * Method sets text for typing
     * @param text of typing
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Method gets date and time when text was typed
     * @return date and time
     */
    public Date getDate() {
        return date;
    }

    /**
     * Method sets date and time when text was typed
     * @param dt - contains data and time
     */
    public void setDate(Date dt) {
        date = dt;
    }

    /**
     * Method gets percentage of errors for typed text
     * @return percentage of errors
     */
    public double getError() {
        return error;
    }

    /**
     * Method sets percentage of errors for typed text
     * @param error percentage of errors
     */
    public void setError(double error) {
        this.error = error;
    }
}
