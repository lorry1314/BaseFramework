package com.musejianglan.baseframework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.musejianglan.chartdemo.R;

import java.util.ArrayList;

/**
 * Created by liulei on 2016/4/26.
 * package:com.musejianglan.chartdemo.view
 * project:ChartDemo
 * <p>
 * 折线图
 * 获取自定义属性
 * <p>
 * X、Y坐标线，刻度
 * 点
 * 线
 * <p>
 * 是否需要刻度线
 * 是否能滑动
 */
public class LineChartView extends View {
    private static final String TAG = "LineChartView";
    private static final int PADDING = 48;
    /**
     *
     */
    private int bgColor;

    /**
     *
     */
    private int lineXYColor;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mRect;
    /**
     * 画笔
     */
    private Paint mPaint;

    private float XScale = 0f; //X轴刻度长度
    private float YScale = 0f; //Y轴刻度长度
    private float XLength = 0f; //X轴长度 根据屏幕设置
    private float YLength = 0f; //Y轴长度 根据屏幕设置
    private float startX = 0; // 坐标原点位置
    private float startY = 0; // 坐标原点位置
    private int xSize = 10; // x轴刻度点数量
    private int ySize = 10; // y轴刻度点数量
    private float lineWidth = 1;
    private float scaleLength = 5;

    private ArrayList<Integer> arrayList;
    private int pointColor = 0xffff0000;
    private int lineColor = 0xffff0000;
    private final float POINTRADIUS = 10;

    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }

    public synchronized void setArrayList(ArrayList<Integer> arrayList) {
        if (arrayList == null) {
           arrayList = new ArrayList<>();
        }
        this.arrayList = arrayList;
        if (arrayList.size() > 0) {
            xSize = arrayList.size();
        } else {
            xSize = 10;
        }

        invalidate();
    }

    public LineChartView(Context context) {
        this(context, null);

    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    /**
     * 初始化。。。
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.style_line_chart);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.style_line_chart_backGroudColor:
                    bgColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.style_line_chart_lineColor:
                    // 默认颜色设置为黑色
                    lineXYColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }

        }

        typedArray.recycle();

        mPaint = new Paint();
        if (lineXYColor <= 0) {
            lineXYColor = Color.BLACK;
        }

        // 抗锯齿  这个方法只适用于消除画的图形，不适用于图片（bitmap等）
        mPaint.setAntiAlias(true);

        mRect = new Rect();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));// 抗锯齿，适合所有 暂时用不到

        initSize();

        //  设置背景色
        if (bgColor > 0) {
            canvas.drawColor(bgColor);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        drawXAndY(canvas);
        drawPoint(canvas);
        drawLine(canvas);


    }

    /**画线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);
        if (arrayList == null) {
            return;
        }

        for (int i = 0;i < arrayList.size() - 1;i ++) {

            Integer y1 = arrayList.get(i);
            Integer y2 = arrayList.get(i + 1);

            //canvas.drawCircle(startX+XScale * (i + 1),YLength / 100 * integer,POINTRADIUS,mPaint);
            canvas.drawLine(startX+XScale * (i + 1), startY - YLength / 100 * y1, startX+XScale * (i + 2), startY - YLength / 100 * y2,mPaint);
        }
    }

    /** 画点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        if (arrayList == null) {
            return;
        }

        mPaint.setColor(pointColor);

        for (int i = 0;i < arrayList.size();i ++) {

            Integer integer = arrayList.get(i);

            canvas.drawCircle(startX+XScale * (i + 1),startY - YLength / 100 * integer,POINTRADIUS,mPaint);
        }


    }

    /**
     * 获取view的大小，并设置相关参数
     */
    private void initSize() {
        int height = getHeight();
        YLength = height - PADDING * 2;
        startY = height - PADDING;
        YScale = YLength / ySize;

        int width = getWidth();
        XLength = width - PADDING * 2;
        startX = PADDING;
        XScale = XLength / xSize;
    }

    /**
     * 绘制x/y轴，及其刻度坐标
     *
     * @param canvas
     */
    private void drawXAndY(Canvas canvas) {

        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(lineXYColor);
        //canvas.drawLine(0,0,100,100,mPaint);

        canvas.drawLine(startX, startY, startX + XLength + scaleLength * 2, startY, mPaint);// x 轴
        for (int i = 1; i <= xSize; i++) {
            canvas.drawLine(startX + XScale * i, startY, startX + XScale * i, startY + scaleLength, mPaint);
            //mPaint.getTextBounds(i * 10 + "", 0, (i * 10 + "").length(), mRect);
            //canvas.drawText(i * 10 + "", startX + XScale * i - mRect.width() / 2, startY + mRect.height() + scaleLength, mPaint);
        }

        canvas.drawLine(startX, startY, startX, startY - YLength - scaleLength * 2, mPaint);// y 轴
        for (int j = 1; j <= ySize; j++) {
            canvas.drawLine(startX, startY - YScale * j, startX + scaleLength, startY - YScale * j, mPaint);
            mPaint.getTextBounds(j * 10 + "", 0, (j * 10 + "").length(), mRect);
            canvas.drawText(j * 10 + "", startX - scaleLength - mRect.width(), startY - YScale * j + mRect.height() / 2, mPaint);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
