package dev.soughtxp.wamodx;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static Context getContext() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app instanceof Context) {
                return (Context) app;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
