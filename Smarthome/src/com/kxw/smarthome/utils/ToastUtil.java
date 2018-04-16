package com.kxw.smarthome.utils;


import com.kxw.smarthome.MyApplication;

import android.widget.Toast;

public class ToastUtil {

    private static Toast toast=null;
     

    public static void showShortToast(String content){
        if(toast!=null) {
            toast.cancel();
        }
        toast=Toast.makeText(MyApplication.getInstances(),content,Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showLongToast(String content){
        if(toast!=null) {
            toast.cancel();
        }
        toast=Toast.makeText(MyApplication.getInstances(),content,Toast.LENGTH_LONG);
        toast.show();
    }
}
