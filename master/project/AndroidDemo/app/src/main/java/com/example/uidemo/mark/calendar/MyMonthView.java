package com.example.uidemo.mark.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;


import com.example.uidemo.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

public class MyMonthView extends MonthView {

    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint3 = new Paint();
    private Context context;
    private Bitmap dayBgBitmap;
    private Bitmap dayErrorBitmap;

    public MyMonthView(Context context) {
        super(context);
        this.context = context;
        //取消文字加粗
        mCurMonthTextPaint.setFakeBoldText(false);
        mOtherMonthTextPaint.setFakeBoldText(false);

        paint1.setColor(ContextCompat.getColor(context, R.color.green));
        paint1.setTextSize(spToPx(context, 13));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setTextAlign(Paint.Align.CENTER);



        paint2.setColor(ContextCompat.getColor(context, R.color.white));
        paint2.setTextSize(spToPx(context, 17));
        paint2.setAntiAlias(true);
        paint2.setTextAlign(Paint.Align.CENTER);

        paint3.setColor(ContextCompat.getColor(context, R.color.myRed));
        paint3.setTextSize(spToPx(context, 17));
//        paint3.setStyle(Paint.Style.STROKE);
        paint3.setAntiAlias(true);
        paint3.setTextAlign(Paint.Align.CENTER);

        dayBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.day_bg1);
        dayErrorBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.day_no);

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        //这里的x、y 是每日的起点坐标
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        if ("1".equals(calendar.getScheme())) {
            //没有做完的
            canvas.drawBitmap(dayErrorBitmap, x + mItemWidth / 4 - 5, y + mItemHeight / 4 + 8, paint2);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, paint2);
        } else if ("2".equals(calendar.getScheme())) {
            //完成的，先绘制背景图再写文字，防止文字被遮住
            canvas.drawBitmap(dayBgBitmap, x + mItemWidth / 4 - 5, y + mItemHeight / 4 + 8, paint2);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, paint2);
        } else {
            //正常日期的显示
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    public static int spToPx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dpToPx(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
