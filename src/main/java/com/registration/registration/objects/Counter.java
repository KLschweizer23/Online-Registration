package com.registration.registration.objects;

public class Counter {
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;

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

    public int getCount4() {
        count4++;
        return count4;
    }

    public boolean resetCount4(){
        count4 = 0;
        return true;
    }
}
