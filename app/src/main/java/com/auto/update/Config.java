package com.auto.update;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Config {
    public static void setUpdateServer(String updateServer) {
        Config.updateServer = updateServer;
    }

    public static String updateServer = "http://192.0.4.81/apps/";

    public static void setUpdateApkname(String updateApkname) {
        Config.updateApkname = updateApkname;
    }

    public static String updateApkname = "YourApp.apk";

    public static void setUpdateVerjson(String updateVerjson) {
        Config.updateVerjson = updateVerjson;
    }

    public static String updateVerjson = "YourApp.json";

    public static void setUpdateSavename(String updateSavename) {
        Config.updateSavename = updateSavename;
    }

    public static String updateSavename = "YourApp.apk";

    public static void setUpdatePackagename(String updatePackagename) {
        Config.updatePackagename = updatePackagename;
    }

    public static String updatePackagename = "com.your.app";

    private static final String TAG = "Config";

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    updatePackagename
                    , 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    updatePackagename
                    , 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;

    }
}
