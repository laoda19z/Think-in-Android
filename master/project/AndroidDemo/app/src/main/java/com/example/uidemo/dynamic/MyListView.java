package com.example.uidemo.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.uidemo.R;

public class MyListView extends ListView {
    private int mImageViewHeight;
    private ImageView mImageView;


    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化一些必要条件
     */
    @SuppressLint("ResourceType")
    private void init() {
        //获取imageview的初始化高度
        mImageViewHeight = getContext().getResources().getDimensionPixelSize(R.mipmap.smiles);
        Log.e("mll","mImageViewHeight"+mImageViewHeight);
    }

    /**
     * 设置imageview
     * @param imageView
     */
    public void setmImageView(ImageView imageView){
        this.mImageView = imageView;
    }

    /**
     * 实现下拉图片变大
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //当deltaY的时候表示向下过度滑动
        if(deltaY < 0){
            Log.e("mll","deltaY"+deltaY);
            int imageViewHeight = mImageView.getHeight() - deltaY;
            mImageView.getLayoutParams().height = imageViewHeight;
            //重新绘制Imageview
            mImageView.requestLayout();
        }
        //当deltaY>0的时候表示上拉过度
        else
        {
            if (mImageView.getHeight() > mImageViewHeight) {
                mImageView.getLayoutParams().height = mImageView.getHeight()
                        - deltaY;
                // 重新绘制，或者叫重新摆放
                mImageView.requestLayout();

            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 实现图片
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    //得到mImageView的父布局，这里明显是headview
        View parent = (View) mImageView.getParent();
        //实际内容的上边界到父布局上边界的距离
        int top = parent.getTop();
        if (top < 0 && mImageView.getHeight() > mImageViewHeight) {

            //计算图片的高度
            int imageViewHeight = mImageView.getHeight() + top;
            mImageView.getLayoutParams().height = imageViewHeight;
            //重新布局
            parent.layout(parent.getLeft(), 0, parent.getRight(), parent.getHeight());
            mImageView.requestLayout();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
    /**
     * 步骤3：实现松手回弹动画
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //这里我们只需要实现UP事件
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //情动回弹动画
            ResetAnimation animation = new ResetAnimation(mImageView);
            animation.setDuration(300);
            mImageView.startAnimation(animation);
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 自定义一个会弹动画
     */
    class ResetAnimation extends Animation {


        private ImageView imageView;
        //高度差
        private int dHeight;

        //图片拉升以后的高度
        private int orginHeight;



        public ResetAnimation(ImageView imageView) {
            this.imageView = imageView;
            orginHeight=this.imageView.getHeight();
            dHeight = orginHeight - mImageViewHeight;
//            Log.w(TAG, "dHeight：" + dHeight);
        }

        /**
         * 这个方法是为一个自定义动画需要重写的方法
         *
         * @param interpolatedTime 动画时间0~1.0
         * @param t
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            Log.w(TAG, "applyTransformation");
//            Log.w(TAG, "interpolatedTime：" + interpolatedTime);
            //设置图片的大小

            //计算图片的高度
            int imageViewHeight = (int) (orginHeight - dHeight * interpolatedTime);
            mImageView.getLayoutParams().height = imageViewHeight;
            mImageView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
}
