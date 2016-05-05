package com.example;

import java.lang.reflect.Field;

public class MyClass {

    public static void main(String[] args) {
        // Class clazz = R.style.class;
        Class clazz = R.style.class;
        for (Field f : clazz.getDeclaredFields()) {
            System.out.println(f.getName());
            try {
                System.out.println(f.getInt(f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public static class R {
        public static class style {
            public static int theme = 300;
        }
    }
}
