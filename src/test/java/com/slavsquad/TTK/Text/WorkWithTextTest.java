package com.slavsquad.TTK.Text;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class WorkWithTextTest {
    private static JTextField imputStr;
    private static JTextArea sourceStr;
    private static WorkWithText wwtTest;

    @BeforeClass
    public static void setUpBeforeClass(){
        imputStr = new JTextField();
        sourceStr = new JTextArea();
        wwtTest = new WorkWithText(imputStr,sourceStr);

    }

    @Test
    public void checkTest(){
        //without erros
        imputStr.setText("Проверка работы модуля работы с текстом.");
        sourceStr.setText("Проверка работы модуля работы с текстом.");
        int expected = 0;
        int actual = wwtTest.check();
        assertEquals(expected,actual);
        //with error
        imputStr.setText("Проверка работы модуля работы с текстАм.");
        actual = wwtTest.check();
        expected=1;
        assertEquals(expected,actual);
    }

//    @Test
//    public void isDoneTest(){
//
//        assertFalse(wwtTest.isDone());
//        //with error
//        imputStr.setText("Прверка работы модуля работы с текстАм.");
//        wwtTest.check();
//       // assertFalse(wwtTest.isDone());
//    }
}
