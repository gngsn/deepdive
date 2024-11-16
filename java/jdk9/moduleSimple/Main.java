package com.example.app;

import com.example.app.api.PublicAccessExample;

public class Main {
    public static void main(String[] args) {
        PublicAccessExample example = new PublicAccessExample();
        
        // Accessing public variable directly
        example.publicVariable = 10;
        
        // Accessing public method
        example.publicMethod();
        
        // Accessing private method - Compiler error
        // example.privateMethod();
    }
}
