package com.tn.tnclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alimjan on 10/27/17.
 */

public class SharedContext {

    private static SharedContext mDemoContext;
    public Context mContext;
    private SharedPreferences mPreferences;

    public static SharedContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new SharedContext();
        }
        return mDemoContext;
    }

    private SharedContext() {
    }

    private SharedContext(Context context) {
        mContext = context;
        mDemoContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static void init(Context context) {
        mDemoContext = new SharedContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

}