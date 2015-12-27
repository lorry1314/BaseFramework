package com.musejianglan.baseframework.utils;


/**
 * Created by liu on 9/9/2015.
 */
public class SharedPreferenceHelper {
    private static String ID = "id";

    public static void setUserId(long userId){
        PreferenceUtil.putLong(ID,userId);
    }

    public static long getUserId(){
        return PreferenceUtil.getLong(ID);
    }


}
