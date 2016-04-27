package com.musejianglan.baseframework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liulei on 2016/4/27.
 * package:com.musejianglan.chartdemo.view
 * project:ChartDemo
 */
public class PieChartView extends View {
    private static final String TAG = "PieChartView";

    private Paint mPaint;
    private float radius;
    private float pieRadius;
    private int padding = 24;

    private int height;
    private int width;
    private int centerHeight = 0;
    private int centerWidth = 0;
    private float roundWidth = 10;
    private int roundProgressColor = 0xff00ff00;
    private float strokeWidth = 20;

    public PieChartView(Context context) {
        this(context,null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, null,0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 属性初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        //TypedArray t

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initData();

        drawCircle(canvas);
        drawPie(canvas);
    }

    /**
     * 绘制饼状区域
     * @param canvas
     */
    private void drawPie(Canvas canvas) {

        //设置进度是实心还是空心
        mPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        mPaint.setColor(roundProgressColor);  //设置进度的颜色
        mPaint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(centerWidth - pieRadius, centerHeight - pieRadius, centerWidth
                + pieRadius, centerHeight + pieRadius);  //用于定义的圆弧的形状和大小的界限

        canvas.drawArc(oval, 0, 360 * 246/ 360, true, mPaint);  //根据进度画圆弧
        /*
        * drawArc
        * oval :指定圆弧的外轮廓矩形区域。
        * startAngle: 圆弧起始角度，单位为度。
        * sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
        * useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
        * paint: 绘制圆弧的画板属性，如颜色，是否填充等。
        * */

        //mPaint.setColor(0xff0000aa);
        //canvas.drawRect(oval,mPaint);



    }

    /**
     * 画圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE); //设置填充
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(centerWidth, centerHeight,radius,mPaint);

        /*
        * stroke转态下：半径为圆环宽度中间值半径
        * */

    }

    /**
     * 初始化数据
     */
    private void initData() {

        height = getHeight();
        width = getWidth();

        centerWidth = width / 2;
        centerHeight = height / 2;

        radius = (height < width ? centerHeight : centerWidth) - padding;//

        pieRadius = radius - strokeWidth / 2;

    }
}
