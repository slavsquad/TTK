package com.slavsquad.TTK;

/**
 * Created by stepanenko.sg on 12.07.2017.
 */
public class FunMath {
    int calls;

    public int getCalls() {
        return calls;
    }

    public long factorial(int number) {
        calls++;

        if (number < 0)
            throw new IllegalArgumentException();

        long result = 1;
        if (number > 1) {
            for (int i = 1; i <= number; i++)
                result = result * i;
        }

        return result;
    }

    public long plus(int num1, int num2) {
        calls++;
        return num1 + num2;
    }
}
