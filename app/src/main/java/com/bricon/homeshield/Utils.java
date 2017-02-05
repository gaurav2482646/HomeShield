package com.bricon.homeshield;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;


public class Utils {
    ConnectivityManager connectivityManager;
    NetworkInfo info;
    public boolean checkConnection(Context context) {
        boolean flag = false;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                System.out.println(info.getTypeName());
                flag = true;
            }
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                System.out.println(info.getTypeName());
                flag = true;
            }
        } catch (Exception exception) {
            System.out.println("Exception at network connection....."
                    + exception);
        }
        return flag;
    }

    ArrayList<DataObject> arrayList;
    public ArrayList getSensorList()
    {
        arrayList=new ArrayList<>();
        arrayList.add(new DataObject("Glass Break Sensor ","test value"));
        arrayList.add(new DataObject("Motion Sensor ","test value"));
        arrayList.add(new DataObject("Light Sensor ","test value"));
        arrayList.add(new DataObject("Smoke Sensor ","test value"));
        arrayList.add(new DataObject("Flood Sensor ","test value"));
        arrayList.add(new DataObject("Light Sensor 2 ","test value"));
        arrayList.add(new DataObject("Glass Break Sensor 2 ","test value"));

        return arrayList;
    }
}
