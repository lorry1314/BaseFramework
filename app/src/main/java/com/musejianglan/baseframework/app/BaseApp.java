package com.musejianglan.baseframework.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by liulei on 2015/12/27.
 */
public class BaseApp extends Application {
    public static final String TAG = "BaseApp";

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        if (!Const.DEBUG) { // release模式抓取系统异常，防止弹窗
            CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
            mCustomCrashHandler.setCustomCrashHanler(mContext);
        }
    }
}
