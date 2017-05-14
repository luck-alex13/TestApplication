package com.android.testapplication.io_package;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Александр on 14.06.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class AppUtil {

    private static String LOG_TAG = "LOG_TAG";

    public static boolean hasConnection(Context appContext) {
        boolean connection = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        connection = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        connection = true;
                    }
                } else {
                    // not connected to the internet
                    connection = false;
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, "AppUtil: has NO Connection() " + ex);
            ex.printStackTrace();
            connection = false;
        }
        return connection;
    }

    public static void requestPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }

    public static boolean isMyServiceRunning(Context appContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void backgroundThreadLongToast(final Context context,
                                                 final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void insertSort(ArrayList<Object> list) {
        int i, j, n;
        Object value;
        /*if (list != null) {
            n = list.size();
            for (i = 1; i < n; i++) {
                value = list.get(i);
                for (j = i - 1; j >= 0 && list.get(j).getStationFreq() > value.getStationFreq(); j--) {
                    list.set(j + 1, list.get(j));
                }
                list.set(j + 1, value);
            }
        }*/
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(cal.getTime());
    }


    public static String getWeekDay(int day) {
        String weekDay = "";
        Calendar c;
        try {
            c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, day);
            weekDay = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("ru", "RU"));
        } catch (NullPointerException ex) {
            weekDay = null;
        } finally {
            // TODO finalize c
        }
        return weekDay;
    }

    public static String getNameOfMonth(int month) throws IllegalArgumentException {
        Calendar c;
        String s;
        try {
            c = Calendar.getInstance();
            c.set(Calendar.MONTH, month);
            s = c.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru", "RU"));
        } catch (NullPointerException ex) {
            s = null;
        } finally {
            // TODO finalize c
        }
        return s;
    }

    public static String getFullDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
