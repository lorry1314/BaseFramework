package com.musejianglan.baseframework.utils;

import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by xmren on 9/6/2015.
 */
public class ViewUtils {
    private static long mLastClickTime = -1;
    private final static long MAX_DOUBLE_CLICK_TIME = 500;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < MAX_DOUBLE_CLICK_TIME) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    public static void changeTextColor(TextView textView,int textColor,int from,int to) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        ForegroundColorSpan changeColorSpan = new ForegroundColorSpan(textView.getContext().getResources().getColor(textColor));
        builder.setSpan(changeColorSpan, from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static void setTextWithCenterLine(String text,TextView tv){
        tv.getPaint().setAntiAlias(true);//抗锯齿
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv.setText("￥"+text);
    }
}
