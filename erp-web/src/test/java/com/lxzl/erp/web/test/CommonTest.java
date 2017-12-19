package com.lxzl.erp.web.test;

public class CommonTest {

    public static void main(String[] args) {
        int value = 0;
        for (int i = 0; i < 10; i++) {
            add(value);
            System.out.println(value);
        }
    }

    private static void add(int value) {
        //这个方法有一些复杂的逻辑，其中value+=1的逻辑只能写在这个方法中
        value ++;
    }
}
