package com.common.ui.exceptiondemo;

import com.common.ui.anrwatchdog.ANRError;
import com.common.ui.anrwatchdog.ANRWatchDog;
import com.common.ui.exception.ExceptionHandler;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application{

    private static final String TAG = Application.class.getSimpleName();
    private Context mContext;
    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();
        Log.d(TAG, "onCreate");
        new ANRWatchDog(10*1000).setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                // Handle the error.
                Log.d(TAG, "onANRListerner enter!!!");
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        Toast.makeText(mContext, "APP is ANR do what you want!", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }.start();
            }
        }).start();
        new ExceptionHandler(mContext).setFCListener(new ExceptionHandler.FCListener() {
            
            @Override
            public void onFCDispose(Throwable paramThrowable) {
                Log.d(TAG, "onFCListerner enter!!!");
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        Toast.makeText(mContext, "APP is Force Close do what you want!", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }.start();
            }
        });
    }
}
