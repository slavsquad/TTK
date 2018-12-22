package com.slavsquad.TTK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Class <>Dictionaries</> implements container which storage list of dictionaries
 */
public class Dictionaries implements Serializable {
    private int currentIdDictionary;
    private List<Dictionaree> listDictionaries= new ArrayList<>();

    /**Construct new path*/
    public Dictionaries() {
        this.currentIdDictionary = 0;
    }

    /**Construct new path*/
    public Dictionaries(int currentIdDictionary, List<Dictionaree> listDictionaries) {
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
    public List<Dictionaree> getListDictionaries() {
        return listDictionaries;
    }

    /**
     * Method sets list of path
     * @param listDictionaries - the list of path*/
    public void setListDictionaries(List<Dictionaree> listDictionaries) {
        this.listDictionaries = listDictionaries;
    }

    /**
     * Method gets current dictionaryLabel
     * @return current dictionaryLabel*/
    public Dictionaree getCurrentDictionary(){
        return listDictionaries.get(currentIdDictionary);
    }

    /**
     * Method adds new dictionaryLabel in list of dictionaries
     * @param dictionary - the dictionaryLabel which adding in list*/
    public void add(Dictionaree dictionary){
        listDictionaries.add(dictionary);
    }

    /**
     * Method gets dictionaryLabel accordance its id
     * @param id - the id number
     * @return dictionaryLabel*/
    public Dictionaree getDictionary(int id){
        return listDictionaries.get(id);
    }

    public Dictionaree getDictionary(String name){
        for (Dictionaree dictionary:listDictionaries){
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
        for (Dictionaree dictionary:listDictionaries){
            listOfNames.add(dictionary.getName());
        }
        return listOfNames;
    }
}
