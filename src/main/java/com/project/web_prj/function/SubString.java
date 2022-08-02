package com.project.web_prj.function;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SubString {

    static String s = "123456789";

    public static void main(String[] args) {
        String s0 = s.substring(0);
        String s2 = s.substring(2);
        String s02 = s.substring(0, 2);
        System.out.println("=============");
        System.out.println(s0);
        System.out.println(s2);
        System.out.println(s02);
        System.out.println("=============");
        String s5 = s.substring(s.lastIndexOf("5"));
        String s4 = s.substring(s.lastIndexOf("4"));
        System.out.println(s5);
        System.out.println(s4);
        String s55 = s.substring(s.indexOf("5"));
        System.out.println(s55);

    }

}
