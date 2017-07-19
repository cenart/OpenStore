package com.ecolemultimedia.openstore.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Cen on 10/07/2017.
 */

public class Preference {
    private static final String PREF_NAME = "name";
    private static final String PREF_DAY = "day";
    private static final String PREF_IMAGE = "image";
    private static final String PREF_START_HOUR = "starthour";
    private static final String PREF_END_HOUR = "endhour";
    private static final String PREF_DESCRIPTION = "description";

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setPrefName(Context context, String name){
        getPreference(context)
                .edit()
                .putString(PREF_NAME, name)
                .commit();
    }

    public static String getPrefName(Context context){
        return getPreference(context).getString(PREF_NAME, null);
    }

    public static void setPrefDay(Context context, String day){
        getPreference(context)
                .edit()
                .putString(PREF_DAY, day)
                .commit();
    }

    public static String getPrefDay(Context context){
        return getPreference(context).getString(PREF_DAY, null);
    }
    public static void setPrefImage(Context context, String image){
        getPreference(context)
                .edit()
                .putString(PREF_IMAGE, image)
                .commit();
    }

    public static String getPrefImage(Context context){
        return getPreference(context).getString(PREF_IMAGE, null);
    }

    public static void setPrefStartHour(Context context, int StartHour){
        getPreference(context)
                .edit()
                .putInt(PREF_START_HOUR, StartHour)
                .commit();
    }
    public static int getPrefStartHour(Context context){
        return getPreference(context).getInt(PREF_START_HOUR, 0);
    }

    public static void setPrefEndHour(Context context, int EndHour){
        getPreference(context)
                .edit()
                .putInt(PREF_END_HOUR, EndHour)
                .commit();
    }
    public static int getPrefEndHour(Context context){
        return getPreference(context).getInt(PREF_END_HOUR, 0);
    }
    public static void setPrefDescription(Context context, String description){
        getPreference(context)
                .edit()
                .putString(PREF_DAY, description)
                .commit();
    }

    public static String getPrefDescription(Context context){
        return getPreference(context).getString(PREF_DESCRIPTION, null);
    }


}
