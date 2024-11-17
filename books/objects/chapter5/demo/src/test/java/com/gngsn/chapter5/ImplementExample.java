package com.gngsn.chapter5;

/**
 * Interface overriding field test
 */

public class ImplementExample implements Example {
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMax(String message) {
        this.message = message;
    }
}