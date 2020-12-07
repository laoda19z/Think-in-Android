package com.example.uidemo.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkVideoView extends FrameLayout {
    private Context mContext;//上下文
    private IMediaPlayer mMediaPlayer = null;//视频控制类
    private VideoPlayerListener mVideoPlayerListener;//自定义监听器
    private SurfaceView mSurfaceView;//播放视图
    private String mPath = "";//视频文件地址

    public IjkVideoView(@NonNull Context context) {
        super(context);
        initVideoView(context);
    }

    public IjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public IjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    public IjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract static class VideoPlayerListener implements IMediaPlayer.OnPreparedListener,
            IMediaPlayer.OnCompletionListener,
            IMediaPlayer.OnErrorListener {
    }

    private void initVideoView(Context context) {
        mContext = context;
        setFocusable(true);
    }

    public void setPath(String path) {
        if (TextUtils.equals("", mPath)) {
            mPath = path;
            initSurfaceView();
        } else {
            mPath = path;
            loadVideo();
        }
    }

    private void initSurfaceView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new LmnSurfaceCallback());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mSurfaceView.setLayoutParams(layoutParams);
        this.addView(mSurfaceView);
    }

    //surfaceView的监听器
    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            loadVideo();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    //加载视频
    private void loadVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        mMediaPlayer = ijkMediaPlayer;

        if (mVideoPlayerListener != null) {
            mMediaPlayer.setOnPreparedListener(mVideoPlayerListener);
            mMediaPlayer.setOnErrorListener(mVideoPlayerListener);
        }
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.prepareAsync();
    }

    public void setListener(VideoPlayerListener listener) {
        this.mVideoPlayerListener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}