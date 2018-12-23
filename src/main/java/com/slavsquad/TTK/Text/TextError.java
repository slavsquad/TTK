package com.slavsquad.TTK.Text;

import java.io.Serializable;

/**
 * Class <code>TextError</code> contains information about errors which made user.
 * Class have private field and public methods.
 * @author  svalSquad
 */
public class TextError implements Serializable{
    private int position;
    private char charError;

    /**
     * Construct new TextError with position error  character error.
     * @param position the character of user's error
     * @param charError the character of user's error
     */
    public TextError(int position, char charError) {
        this.position = position;
        this.charError = charError;
    }

    /**
     * Method return position error character.
     * @return position the character of user's error
     */
    public int getPosition() {
        return position;
    }

    /**
     * Method sets position error character.
     * @param position the character of user's error
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Method returns error's character.
     * @return character of user's error
     */
    public char getCharError() {
        return charError;
    }

    /**
     * Method sets error's character.
     * @param charError the character of user's error
     */
    public void setCharError(char charError) {
        this.charError = charError;
    }

    /**
     * Method returns string version for object TextError.
     * @return information for position and charError
     */
    @Override
    public String toString() {
        return "TextError{" +
                "position=" + position +
                ", charError - " + charError +
                '}';
    }

}
