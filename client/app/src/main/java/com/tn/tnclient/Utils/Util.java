package com.tn.tnclient.Utils;

import android.content.Context;

/**
 * Created by alimjan on 10/17/17.
 */

public class Util {
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }
}
