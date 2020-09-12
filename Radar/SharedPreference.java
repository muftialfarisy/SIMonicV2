package com.KP.simonicv2.Radar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setEnablepermission(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(Constant.keyshared, loggedIn);
        editor.apply();
    }

    public static boolean getEnablepermission(Context context) {
        return getPreferences(context).getBoolean(Constant.keyshared, false);
    }

    public static void setsharedinterval(Context context, int interval) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(Constant.keysharedinterval, interval);
        editor.apply();
    }

    public static int getsharedinterval(Context context) {
        return getPreferences(context).getInt(Constant.keysharedinterval, 0);
    }

    public static void setsharedradius(Context context, int interval) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(Constant.keysharedradius, interval);
        editor.apply();
    }

    public static int getsharedradius(Context context) {
        return getPreferences(context).getInt(Constant.keysharedradius, 1);
    }
}
