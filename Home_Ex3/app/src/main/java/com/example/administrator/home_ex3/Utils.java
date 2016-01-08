package com.example.administrator.home_ex3;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 1/8/2016.
 */
public class Utils {

    public static ProgressDialog dialog;
    public static void showProgressDialog(Context context, String message){
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void cancelProgressDialog(){
        if (dialog != null){
            dialog.cancel();
            dialog = null;
        }
    }



}
