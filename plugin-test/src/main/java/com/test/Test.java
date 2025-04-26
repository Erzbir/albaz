package com.test;

import com.google.gson.Gson;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        new Test().say();
    }

    public void say() {
        Gson gson = new Gson();
        String json = gson.toJson("""
                {"name": "test"}
                """, String.class);
        System.out.println("This is a plugin test method");
        System.out.println("Dependency test output: " + json);
    }
}
