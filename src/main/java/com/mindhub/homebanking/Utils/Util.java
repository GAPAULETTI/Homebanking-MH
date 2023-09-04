package com.mindhub.homebanking.Utils;

public class Util {

    public static String generateNumberAccount(){
        String prefix = "VIN-";
        int number = (int)(10000000 + (Math.random()*89999999));
        return prefix + number;
    }

    public static String generateNumberCard(){
        int max = 9999;
        int min = 1000;
        int num1 = (int) (1000 + (Math.random()*8999));
        int num2 = (int) (1000 + (Math.random()*8999));
        int num3 = (int) (1000 + (Math.random()*8999));
        int num4 = (int) (1000 + (Math.random()*8999));
        return num1 + " " + num2 + " " + num3 + " " + num4;
    }
    public static int cvvNumber(){
        return (int)(100 + (Math.random()*999));
    }
}
