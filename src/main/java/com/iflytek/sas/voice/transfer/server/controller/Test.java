package com.iflytek.sas.voice.transfer.server.controller;

/**
 * @author: JiangPing Li
 * @date: 2018-08-24 10:13
 */
public class Test {
    public static void main(String[] args) {
//        int result = fact(10,1);
//        System.out.println(result);
        int a = 129;
        byte b = (byte)a;
        System.out.println(b);
    }

    private static int fact(int n,int a){
        if (n < 0)
        {return 0;}
        else if (n == 0){
            return 1;}
        else if (n == 1){
            return a;}
        else{
            return fact(n - 1, n * a);}
    }
}
