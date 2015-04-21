
package com.common.ui.exceptiondemo;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private final Object mutex = new Object();

    private static void SleepAMinute() {
        try {
            Thread.sleep(60 * 1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class LockerThread extends Thread {

        public LockerThread() {
            setName("APP: Locker");
        }

        @Override
        public void run() {
            synchronized (mutex) {
                //noinspection InfiniteLoopStatement
                while (true)
                    SleepAMinute();
            }
        }
    }

    private void deadLock() {
        new LockerThread().start();

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                synchronized (mutex) {
                    Log.e("ANR-Failed", "There should be a dead lock before this message");
                }
            }
        }, 1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton01 = (Button) findViewById(R.id.btn01);
        mButton01.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                throw new NullPointerException();
            }

        });
        Button mButton02 = (Button) findViewById(R.id.btn02);;
        mButton02.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                deadLock();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
