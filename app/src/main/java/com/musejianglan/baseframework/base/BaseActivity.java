package com.musejianglan.baseframework.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.musejianglan.baseframework.annotation.ViewInject;
import com.musejianglan.baseframework.app.AppManager;

import java.lang.reflect.Field;

/**
 * Created by liulei on 2015/10/22.
 */
public abstract class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        dealAfterInitView(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity();
    }

    public abstract int getLayoutRes();

    protected abstract void dealAfterInitView(Bundle savedInstanceState);

    private void initView() {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();//获得Activity中声明的字段
        for (Field filed : fields){
            // 查看这个字段是否有我们自定义的注解类标志的
            if (filed.isAnnotationPresent(ViewInject.class)){
                ViewInject viewInject = filed.getAnnotation(ViewInject.class);
                if (viewInject != null) {
                    int id = viewInject.value();
                        if (id > 0){
                            try {
                                View view = findViewById(id);
                                filed.setAccessible(true);
                                filed.set(this,view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                }
            }



        }
    }


}
