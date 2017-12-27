package com.lxzl.erp.web.test;

public class Counter {
    static class C {
        int i = 0;
    }

    public static void main(String[] args) {
        C c = new Counter.C();
        for (int i = 0; i < 10; i++) {
            changeCount(c);
            System.out.println(c.i);
        }
    }

    static void changeCount(C c) {
        c.i = c.i + 100;
    }
}
