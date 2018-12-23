package com.slavsquad.TTK.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Class <>Dictionaries</> implements container which storage list of dictionaries
 */
public class Dictionaries implements Serializable {
    private int currentIdDictionary;
    private List<Dictionary> listDictionaries= new ArrayList<>();

    /**Construct new path*/
    public Dictionaries() {
        this.currentIdDictionary = 0;
    }

    /**Construct new path*/
    public Dictionaries(int currentIdDictionary, List<Dictionary> listDictionaries) {
        this.currentIdDictionary = currentIdDictionary;
        this.listDictionaries = listDictionaries;
    }

    /**
     * Method gets id current dictionaryLabel
     * @return id current dictionaryLabel*/
    public int getCurrentIdDictionary() {
        return currentIdDictionary;
    }

    /**
     * Method sets id current dictionaryLabel
     * @param currentIdDictionary - the id number of current dictionaryLabel*/
    public void setCurrentIdDictionary(int currentIdDictionary) {
        this.currentIdDictionary = currentIdDictionary;
    }

    /**
     * Method gets list of path
     * @return list of path*/
    public List<Dictionary> getListDictionaries() {
        return listDictionaries;
    }

    /**
     * Method sets list of path
     * @param listDictionaries - the list of path*/
    public void setListDictionaries(List<Dictionary> listDictionaries) {
        this.listDictionaries = listDictionaries;
    }

    /**
     * Method gets current dictionaryLabel
     * @return current dictionaryLabel*/
    public Dictionary getCurrentDictionary(){
        return listDictionaries.get(currentIdDictionary);
    }

    /**
     * Method adds new dictionaryLabel in list of dictionaries
     * @param dictionary - the dictionaryLabel which adding in list*/
    public void add(Dictionary dictionary){
        listDictionaries.add(dictionary);
    }

    /**
     * Method gets dictionaryLabel accordance its id
     * @param id - the id number
     * @return dictionaryLabel*/
    public Dictionary getDictionary(int id){
        return listDictionaries.get(id);
    }

    public Dictionary getDictionary(String name){
        for (Dictionary dictionary:listDictionaries){
            if (dictionary.getName().equals(name)){
                return dictionary;
            }
        }
        return null;
    }

    /**
     * Method gets array of names for all dictionaries
     * @return listOfNames array contains names of dictionaries*/
    public Vector<String> getNames(){
        Vector<String> listOfNames = new Vector<>();
        for (Dictionary dictionary:listDictionaries){
            listOfNames.add(dictionary.getName());
        }
        return listOfNames;
    }
}
