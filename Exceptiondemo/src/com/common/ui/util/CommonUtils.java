package com.common.ui.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class CommonUtils {
    public static boolean isSdcardExist() {
        boolean isExist = false;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            isExist = true;
        }
        return isExist;
    }
    public static void restartCurrentApp(Context context,String packageName, String className) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(Intent.ACTION_MAIN);
        mIntent.setClassName(packageName, className);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, mIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
        long now = System.currentTimeMillis();
        am.setInexactRepeating(AlarmManager.RTC, now, 3000, pi);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
