package com.musejianglan.baseframework;


import android.os.Bundle;
import android.view.View;

import com.musejianglan.baseframework.annotation.ViewInject;
import com.musejianglan.baseframework.base.BaseActivity;
import com.musejianglan.baseframework.utils.ToastUtil;
import com.musejianglan.baseframework.widget.TitleBar;

public class MainActivity extends BaseActivity {

    @ViewInject(R.id.titlebar)
    private TitleBar titlebar;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void dealAfterInitView(Bundle savedInstanceState) {

        titlebar.setOnLeftImgBackClickListener(new TitleBar.OnLeftImgBackClickListener() {
            @Override
            public void onLeftImgBackClick(View v) {
                ToastUtil.showNormalShortToast("左图");
            }
        });
        titlebar.setOnLeftTxtBtnClickListener(new TitleBar.OnLeftTxtBtnClickListener() {
            @Override
            public void onLeftTxtBtnClick(View v) {
                ToastUtil.showNormalShortToast("左字");
            }
        });

        titlebar.setOnRightImgClickListener(new TitleBar.OnRightImgClickListener() {
            @Override
            public void onRightImgClick(View v) {
                ToastUtil.showNormalShortToast("右图");
            }
        });

        titlebar.setOnRightTxtBtnClickListener(new TitleBar.OnRightTxtBtnClickListener() {
            @Override
            public void onRightTxtBtnClick(View v) {
                ToastUtil.showNormalShortToast("右字");
            }
        });
    }


}
