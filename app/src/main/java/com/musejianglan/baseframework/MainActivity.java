package com.musejianglan.baseframework;


import android.os.Bundle;

import com.musejianglan.baseframework.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void dealAfterInitView(Bundle savedInstanceState) {

    }


}
