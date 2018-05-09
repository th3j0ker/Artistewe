package com.typhon.studios.artistewe_prototype_3.Utils;

/**
 * Created by root on 2/12/17.
 */




/*
*
*
* ONLY USE FOR USER NAMES NOT IDENTITY NUMBER
*
*
*
*
* */
public class StringManipulation {
    public static String expandUsername(String username){
        return username.replace("."," ");
    }
    public static String condenseUsername(String username){
        return username.replace(" ",".");
    }
}
