package com.musejianglan.baseframework.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.musejianglan.baseframework.R;

/**
 * Created by liulei on 2016/3/18.
 * 左右各有一个imageview 和 textview ，除了左边的imageview和title默认显示，其他的都gone调，
 */
public class TitleBar extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "TitleBar";

    private View titleBar;// TitleBar视图

    private TextView titleTxt;// Title名称
    private String title;// title
    private Activity activity;// 当前TitleBar所在的Activity
    private float titleSize;
    private int titleColor;

    private TextView rightTextBtn;// 右边的文字按钮
    private boolean showRightTxtBtn;// 是否显示文字按钮,默认不显示
    private String rightTextBtnStr;// 文字按钮的具体文字
    private OnRightTxtBtnClickListener onRightTxtBtnClickListener;// 文字按钮点击的回调接口
    private int right_img_src;

    private ImageView rightImg;// 右边的图片按钮
    private boolean showRightImg;// 是否显示文字按钮,默认不显示
    private OnRightImgClickListener onRightImgClickListener;// 文字按钮点击的回调接口


    private ImageView leftBackImgBtn;// 返回按钮
    private OnLeftImgBackClickListener onLeftImgBackClickListener;// 点击返回按钮
    private boolean showLeftImgBtn;
    private int left_img_src;

    private TextView leftTextBtn;// 左边的文字按钮
    private boolean showLeftTxtBtn;// 是否显示左边文字按钮,默认不显示
    private String leftTextBtnStr;// 左边文字按钮的具体文字
    private OnLeftTxtBtnClickListener onLeftTxtBtnClickListener;// 文字按钮点击的回调接口

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getValues(context, attrs);
        initView(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        getValues(context, attrs);
        initView(context);
    }

    public void setTittleText(String title) {
        titleTxt.setText(title);
    }

    /**
     * 获取属性值
     *
     * @param attrs
     */
    private void getValues(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TitleBar);
        title = ta.getString(R.styleable.TitleBar_title_txt);
        showRightTxtBtn = ta.getBoolean(R.styleable.TitleBar_show_right_txt_btn, false);
        showRightImg = ta.getBoolean(R.styleable.TitleBar_show_right_img, false);
        rightTextBtnStr = ta.getString(R.styleable.TitleBar_right_txt_btn_str);
        showLeftImgBtn = ta.getBoolean(R.styleable.TitleBar_show_left_img_btn, true);

        showLeftTxtBtn = ta.getBoolean(R.styleable.TitleBar_show_left_txt_btn,false);
        leftTextBtnStr = ta.getString(R.styleable.TitleBar_left_txt_btn_str);

        right_img_src = ta.getResourceId(R.styleable.TitleBar_right_img_src, 0);
        left_img_src = ta.getResourceId(R.styleable.TitleBar_left_img_src, 0);

        titleSize = ta.getDimension(R.styleable.TitleBar_titleSize,16);
        //titleColor = ta.getColor(R.styleable.TitleBar_titleColor,Color.BLACK);
        titleColor = ta.getColor(R.styleable.TitleBar_titleColor,Color.RED);
        ta.recycle();
    }

    /**
     * 初始化视图
     *
     * @param context
     */
    private void initView(Context context) {
        activity = (Activity) context;// 获取当前TitleBar所在的Activity
        titleBar = View.inflate(context, R.layout.widget_action_bar, null);
        leftBackImgBtn = (ImageView) titleBar.findViewById(R.id.back_btn);
        rightImg = (ImageView) titleBar.findViewById(R.id.right_img_xxx);
        titleTxt = (TextView) titleBar.findViewById(R.id.title_txt);
        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

        if (titleSize > 0) {
            titleTxt.setTextSize(titleSize);
        }

        titleTxt.setTextColor(titleColor);
        leftBackImgBtn.setOnClickListener(this);

        rightTextBtn = (TextView) titleBar.findViewById(R.id.right_txt_btn);
        leftTextBtn = (TextView) titleBar.findViewById(R.id.left_txt_btn);

        // 是否右边显示文字按钮
        if (showRightTxtBtn) {
            rightTextBtn.setVisibility(View.VISIBLE);
            if (rightTextBtnStr != null) {
                rightTextBtn.setText(rightTextBtnStr);
            }
            rightTextBtn.setOnClickListener(this);
        } else {
            rightTextBtn.setVisibility(View.GONE);
        }
        //显示左边文字按钮
        if (showLeftTxtBtn) {
            leftTextBtn.setVisibility(View.VISIBLE);
            if (leftTextBtnStr != null) {
                leftTextBtn.setText(leftTextBtnStr);
            }
            leftTextBtn.setOnClickListener(this);
        } else {
            leftTextBtn.setVisibility(View.GONE);
        }
        //显示右边图片按钮
        if (showRightImg) {

            rightImg.setVisibility(View.VISIBLE);
            if (rightImg != null && right_img_src > 0) {
                rightImg.setImageResource(right_img_src);
            }
            rightImg.setOnClickListener(this);

        } else {
            rightImg.setVisibility(View.GONE);
        }
        //显示左
        if (showLeftImgBtn) {
            leftBackImgBtn.setVisibility(View.VISIBLE);
            if (leftBackImgBtn != null && left_img_src > 0) {
                leftBackImgBtn.setImageResource(left_img_src);
            }
        } else {
            leftBackImgBtn.setVisibility(View.GONE);

        }

        addView(titleBar, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public void back() {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:

                if (onLeftImgBackClickListener != null) {
                    onLeftImgBackClickListener.onLeftImgBackClick(v);
                } else {
                    back();
                }
                break;

            case R.id.right_img_xxx:
                if (onRightImgClickListener != null) {
                    onRightImgClickListener.onRightImgClick(v);
                }

                break;
            // 文字按钮点击事件
            case R.id.right_txt_btn:
                if (onRightTxtBtnClickListener != null) {
                    onRightTxtBtnClickListener.onRightTxtBtnClick(v);
                }

                break;
            case R.id.left_txt_btn:
                if (onLeftTxtBtnClickListener != null) {
                    onLeftTxtBtnClickListener.onLeftTxtBtnClick(v);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 文字按钮点击的回调接口
     *
     * @author Hzp
     * @data 2015-4-18 上午11:53:31
     */
    public interface OnRightTxtBtnClickListener {
        public void onRightTxtBtnClick(View v);
    }

    public interface OnLeftTxtBtnClickListener {
        public void onLeftTxtBtnClick(View v);
    }

    public interface OnRightImgClickListener {
        public void onRightImgClick(View v);
    }

    public interface OnLeftImgBackClickListener {
        public void onLeftImgBackClick(View v);
    }

    /**
     * 设置文字按钮点击监听
     *
     * @param onLeftImgBackClickListener
     */

    public void setOnLeftImgBackClickListener(OnLeftImgBackClickListener onLeftImgBackClickListener) {
        this.onLeftImgBackClickListener = onLeftImgBackClickListener;
    }

    public void setOnRightTxtBtnClickListener(
            OnRightTxtBtnClickListener onRightTxtBtnClickListener) {
        this.onRightTxtBtnClickListener = onRightTxtBtnClickListener;
    }
    public void setOnLeftTxtBtnClickListener(
            OnLeftTxtBtnClickListener onleftTxtBtnClickListener) {
        this.onLeftTxtBtnClickListener = onleftTxtBtnClickListener;
    }

    public void setOnRightImgClickListener(OnRightImgClickListener onRightImgClickListener) {
        this.onRightImgClickListener = onRightImgClickListener;
    }
}
