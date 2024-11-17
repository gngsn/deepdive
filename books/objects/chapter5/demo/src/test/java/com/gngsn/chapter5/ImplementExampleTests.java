package com.gngsn.chapter5;

/**
 * Interface overriding field test
 */
public class ImplementExampleTests implements Example {
    public static void main(String[] args) {
        ImplementExample ex = new ImplementExample();
        ex.setMax("change static final field defined interface");

        System.out.println(ex.getMessage());
    }
}