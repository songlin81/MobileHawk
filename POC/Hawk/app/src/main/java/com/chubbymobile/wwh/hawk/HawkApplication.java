package com.chubbymobile.wwh.hawk;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HawkApplication extends Application {

    private static HawkApplication hwApp;
    public static final int SUCCESS = 100001;

    @Override
    public void onCreate() {
        super.onCreate();
        hwApp = this;
    }

    public static HawkApplication getInstance()
    {
        return hwApp;
    }

    public static void initData(final Handler mHandler){
        new Thread() {
            @Override
            public void run() {
                super.run();

                Bundle bundle = new Bundle();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                bundle.putString("Version", "0.0.1");
                bundle.putString("TimeStamp",df.format(date));

                Message message = Message.obtain();
                message.setData(bundle);
                message.what=SUCCESS;

                mHandler.sendMessage(message);
            }
        }.start();
    }
}
