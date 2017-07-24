package com.nilin.simpleweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.nilin.simpleweather.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class LineView extends View {

    /**
     * view的总高度
     */
    private int mViewHeight;

    /**
     * view的总宽度
     */
    private int mViewWidth;

    /**
     * 温度字体大小
     */
    private int mTempTextSize = 40;

    /**
     * 温度字体颜色
     */
    private int mTempTextColor = Color.BLACK;

    /**
     * 线的宽度
     */
    private int mWeaLineWidth = 7;

    /**
     * 圆点的半径
     */
    private int mWeaDotRadius = 8;

    /**
     * 画圆圈的画笔与画线的笔
     */
    private Paint mDotPaint;

    private Paint mLinePaint;

    /**
     * 画灰色线的笔与画温度的笔
     */
    private TextPaint mTempPaint;

    /**
     * 文字和点的间距
     */
    private int mTextDotDistance = 30;

    /**
     * 坐标点温度文字偏移量
     */
    private static final int POINT_TEXT_OFFSET = 18;

    /**
     * 最高温集合中温度差
     */
    private float mHighsTempest;

    /**
     * 最低温集合中温度差
     */
    private int mLowsTempest;

    /**
     * 最高温数组
     */
    private List<Integer> mHighs;

    /**
     * 最低温数组
     */
    private List<Integer> mLows;

    /**
     * 与顶部和底部的间距
     */
    private final int mMarginTopAndrBottom = 30;

    /**
     * 每个点的间隔X轴
     */
    private int mInterval;

    /**
     * 第一个点的X坐标
     */
    private float mFristX;

    /**
     * 折线图高度（单个）
     */
    private float mLineHeight;

    /**
     * 调节变量
     */
    private int mSpace;

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        initView();     //首次启动初始化数据
        computeSize();
    }

    /**
     * 计算相关大小
     */
    private void computeSize() {
        mInterval = mViewWidth / mHighs.size();
        mFristX = mInterval / 2;
        mLineHeight = mViewHeight / 4;
        mSpace = mViewHeight / 4;
    }

    private int mHighsLowest;

    /**
     * 设置七天中高温里面最高温与最低温
     */
    public void setHighsest(int highest, int lowest) {
        this.mHighsLowest = lowest;
        if (lowest <= 0 && highest >= 0) {
            mHighsTempest = highest + Math.abs(lowest);
        } else if (highest <= 0) {
            mHighsTempest = Math.abs(highest) - Math.abs(lowest);
        } else if (highest >= 0 && lowest >= 0) {
            mHighsTempest = highest - lowest;
        }
    }

    private int mLowsLowsest;

    /**
     * 设置七天中低温里面最高温与最低温
     */
    public void setLowsest(int highest, int lowest) {
        this.mLowsLowsest = lowest;
        if (lowest <= 0 && highest >= 0) {
            mLowsTempest = highest + Math.abs(lowest);
        } else if (highest <= 0) {
            mLowsTempest = Math.abs(highest) - Math.abs(lowest);
        } else if (highest >= 0 && lowest >= 0) {
            mLowsTempest = highest - lowest;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
        initView();     //刷新后初始化数据
        drawLine(canvas);
        drawDot(canvas);
        drawTemp(canvas);
    }

    /**
     * 初始化各个画笔
     */
    private void initPaint() {
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(getResources().getColor(R.color.drawDot));

        mTempPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTempPaint.setTextSize(mTempTextSize);
        mTempPaint.setColor(mTempTextColor);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mWeaLineWidth);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(getResources().getColor(R.color.lineHigh)); //绘制高温线颜色
    }

    private void drawLine(Canvas canvas) {
        float highsBaseY = mLineHeight / mHighsTempest;//最高温中每隔一度对应相隔多少y坐标
        float lowsBaseY = mLineHeight / mLowsTempest;//最低温中每隔一度对应相隔多少y坐标
        float y1, y2 = 0f;//y1起点y坐标，y2 终点y坐标
        float x1, x2 = 0f;

        //绘制高温
        for (int i = 0; i < mHighs.size() - 1; i++) {
            x1 = mFristX + mInterval * i;
            x2 = mFristX + mInterval * (i + 1);

            y1 = mHighs.get(i) - mHighsLowest;
            y1 = highsBaseY * y1 - (mMarginTopAndrBottom * 3);
            y1 = mLineHeight - y1;

            y2 = mHighs.get(i + 1) - mHighsLowest;
            y2 = highsBaseY * y2 - (mMarginTopAndrBottom * 3);
            y2 = mLineHeight - y2;
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawLine(x1, y1, x2, y2, mLinePaint);
        }

        //绘制低温
        mLinePaint.setColor(getResources().getColor(R.color.lineLow));  //绘制低温线颜色
        for (int i = 0; i < mLows.size() - 1; i++) {
            x1 = mFristX + mInterval * i;
            x2 = mFristX + mInterval * (i + 1);

            y1 = mLows.get(i) - mLowsLowsest;
            y1 = lowsBaseY * y1 - (mMarginTopAndrBottom * 3);
            y1 = mLineHeight - y1 + mSpace;

            y2 = mLows.get(i + 1) - mLowsLowsest;
            y2 = lowsBaseY * y2 - (mMarginTopAndrBottom * 3);
            y2 = mLineHeight - y2 + mSpace;
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawLine(x1, y1, x2, y2, mLinePaint);
        }
    }

    private void drawDot(Canvas canvas) {
        float highsBaseY = mLineHeight / mHighsTempest;//最高温中每隔一度对应相隔多少y坐标
        float lowsBaseY = mLineHeight / mLowsTempest;//最低温中每隔一度对应相隔多少y坐标
        float y = 0f;
        float x = 0f;
        for (int i = 0; i < mHighs.size(); i++) {
            y = mHighs.get(i) - mHighsLowest;
            x = mFristX + mInterval * i;
            y = highsBaseY * y - (mMarginTopAndrBottom * 3);
            y = mLineHeight - y;
            canvas.drawCircle(x, y, mWeaDotRadius, mDotPaint);
        }

        for (int i = 0; i < mLows.size(); i++) {
            y = mLows.get(i) - mLowsLowsest;
            y = lowsBaseY * y - (mMarginTopAndrBottom * 3);
            y = mLineHeight - y + mSpace;
            canvas.drawCircle(mFristX + mInterval * i, y, mWeaDotRadius, mDotPaint);
        }
    }

    /**
     * 绘制温度（文本）
     */
    private void drawTemp(Canvas canvas) {
        float baseY = mLineHeight / mHighsTempest;//每隔一度对应相隔多少y坐标
        float lowsBaseY = mLineHeight / mLowsTempest;//最低温中每隔一度对应相隔多少y坐标
        float y = 0f;
        float x = 0f;
        for (int i = 0; i < mHighs.size(); i++) {
            y = mHighs.get(i) - mHighsLowest;
            y = baseY * y - (mMarginTopAndrBottom * 3);
            y = mLineHeight - y - mTextDotDistance;
            x = mFristX + mInterval * i - POINT_TEXT_OFFSET;
            canvas.drawText(String.valueOf(mHighs.get(i)), x, y, mTempPaint);
        }

        for (int i = 0; i < mHighs.size(); i++) {
            y = mLows.get(i) - mLowsLowsest;
            y = lowsBaseY * y - (mMarginTopAndrBottom * 3);
            y = mLineHeight - y + mSpace + mTextDotDistance + 10;
            x = mFristX + mInterval * i - POINT_TEXT_OFFSET;
            canvas.drawText(String.valueOf(mLows.get(i)), x, y, mTempPaint);
        }
    }

    public void initView() {
        mHighs = new ArrayList<>();
        mLows = new ArrayList<>();

        SharedPreferences pref = getContext().getSharedPreferences("weather_pref", MODE_PRIVATE);

        mHighs.add(pref.getInt("temperature_max1", 0));
        mHighs.add(pref.getInt("temperature_max2", 0));
        mHighs.add(pref.getInt("temperature_max3", 0));
        mHighs.add(pref.getInt("temperature_max4", 0));
        mHighs.add(pref.getInt("temperature_max5", 0));
        mHighs.add(pref.getInt("temperature_max6", 0));
        mHighs.add(pref.getInt("temperature_max7", 0));
        mHighs.add(pref.getInt("temperature_max8", 0));
        mHighs.add(pref.getInt("temperature_max9", 0));

        mLows.add(pref.getInt("temperature_min1", 0));
        mLows.add(pref.getInt("temperature_min2", 0));
        mLows.add(pref.getInt("temperature_min3", 0));
        mLows.add(pref.getInt("temperature_min4", 0));
        mLows.add(pref.getInt("temperature_min5", 0));
        mLows.add(pref.getInt("temperature_min6", 0));
        mLows.add(pref.getInt("temperature_min7", 0));
        mLows.add(pref.getInt("temperature_min8", 0));
        mLows.add(pref.getInt("temperature_min9", 0));

        int aMax = Collections.max(mHighs);
        int aMin = Collections.min(mHighs);
        int bMax = Collections.max(mLows);
        int bMin = Collections.min(mLows);

        setHighsest(aMax, aMin);
        setLowsest(bMax, bMin);
    }

}
