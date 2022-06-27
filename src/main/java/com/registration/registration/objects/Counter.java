package com.registration.registration.objects;

public class Counter {
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;

    public int getCount1() {
        count1++;
        return count1;
    }

    public int getCount2() {
        count2++;
        return count2;
    }

    public int getCount3() {
        count3++;
        return count3;
    }
}
