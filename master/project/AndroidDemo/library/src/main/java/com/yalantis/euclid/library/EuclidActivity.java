package com.yalantis.euclid.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.coursedemo.bean.Course;
import com.nhaarman.listviewanimations.appearance.ViewAnimator;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */

public abstract class EuclidActivity extends Activity {

    //显示动画持续时间
    private static final int REVEAL_ANIMATION_DURATION = 1000;
    //最大延迟显示细节动画
    private static final int MAX_DELAY_SHOW_DETAILS_ANIMATION = 500;
    //动画持续时间显示个人资料细节
    private static final int ANIMATION_DURATION_SHOW_PROFILE_DETAILS = 500;
    //步骤延迟隐藏细节动画
    private static final int STEP_DELAY_HIDE_DETAILS_ANIMATION = 80;
    //关闭详情页的时间
    private static final int ANIMATION_DURATION_CLOSE_PROFILE_DETAILS = 500;
    //小图标打开的时间
    private static final int ANIMATION_DURATION_SHOW_PROFILE_BUTTON = 300;
    //圆形头像显示的半径
    private static final int CIRCLE_RADIUS_DP = 50;

    //可以被继承者直接使用
    protected RelativeLayout mWrapper;
    protected ListView mListView;
    protected FrameLayout mToolbar;
    protected RelativeLayout mToolbarProfile;
    protected RelativeLayout mProfileDetails;
    protected TextView mTextViewProfileName;
    protected TextView mTextViewProfileDescription;
    protected View mButtonProfile;//详情页中的小信息图标


    public static ShapeDrawable sOverlayShape;//用于画椭圆，圆形头像
    static int sScreenWidth;//屏幕宽 单位是像素
    static int sProfileImageHeight;//图片高度

    private SwingLeftInAnimationAdapter mListViewAnimationAdapter;//设置滑动的动画效果
    private ViewAnimator mListViewAnimator;//

    private View mOverlayListItemView;
    private EuclidState mState = EuclidState.Closed;

    //获取按钮距离父容器左边的距离，即视图坐标
    private float mInitialProfileButtonX;

    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;
    private Animation mProfileButtonShowAnimation;

    //数据列表
    private List<Course> courses;
    private Handler myHandler;

    private Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euclid);

        //收到服务端传来的课程数据
        myHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        courses = (List<Course>) msg.obj;
                        //初始化列表信息和功能动作
                        initList();
                        break;
                }
            }
        };

        //获取服务端的课程数据
        downLoadCourses();

        //进行view的初始化
        mWrapper = (RelativeLayout) findViewById(R.id.wrapper);//主界面的RL布局（整体）
        mListView = (ListView) findViewById(R.id.list_view);//主界面中的list列表（未跳转）
        mToolbar = (FrameLayout) findViewById(R.id.toolbar_list);//标题
        mToolbarProfile = (RelativeLayout) findViewById(R.id.toolbar_profile);//跳转之后的标题布局
        mProfileDetails = (RelativeLayout) findViewById(R.id.wrapper_profile_details);//跳转之后详细信息的布局
        mTextViewProfileName = (TextView) findViewById(R.id.text_view_profile_name);//姓名
        mTextViewProfileDescription = (TextView) findViewById(R.id.text_view_profile_description);//详细信息
        mButtonProfile = findViewById(R.id.button_profile);//小图标
        btnReturn = findViewById(R.id.btn_phRet);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EuclidActivity.this.finish();
            }
        });

        //用 View.post() or View.postDelay() 来代替 Handler 使用。
        mButtonProfile.post(new Runnable() {
            @Override
            public void run() {
                mInitialProfileButtonX = mButtonProfile.getX();//获取点击事件距离控件左边的距离，即视图坐标
            }
        });

        //点击了返回按钮 点击了返回
        findViewById(R.id.toolbar_profile_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCloseProfileDetails();
            }
        });

        sScreenWidth = getResources().getDisplayMetrics().widthPixels;//获取屏幕宽度
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);//设置图片高度
        sOverlayShape = buildAvatarCircleOverlay();//椭圆


    }

    /**
     * 与数据库连接获取课程信息
     * */
    public void downLoadCourses(){
        new Thread(){
            @Override
            public void run() {
                List<Course> courses = new ArrayList<>();
                try{
                    URL url = new URL("http://39.103.131.145:8080/DynamicDemo/Course");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in,"utf-8")
                    );
                    String result = reader.readLine();
                    Log.e("mll",result);
                    reader.close();

                    JSONObject jCourses = new JSONObject(result);
                    JSONArray jArray = jCourses.getJSONArray("courses");
                    for(int i=0;i<jArray.length();i++) {
                        Course course = new Course();
                        JSONObject jCourse = jArray.getJSONObject(i);
                        int id = jCourse.getInt("id");
                        String name = jCourse.getString("name");
                        int num = jCourse.getInt("num");
                        String content = jCourse.getString("content");
                        String time = jCourse.getString("time");
                        String type = jCourse.getString("type");

                        course.setCourseId(id);
                        course.setCourseName(name);
                        course.setCoursePeoNum(num);
                        course.setCourseContent(content);
                        course.setCourseTime(time);
                        course.setCourseType(type);
                        courses.add(course);
                    }
                    Message msg = myHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = courses;
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 滑入的动画效果
     * item点击动画效果
     */
    private void initList() {
        //滑动效果Adapter
        mListViewAnimationAdapter = new SwingLeftInAnimationAdapter(getAdapter(courses));
        mListViewAnimationAdapter.setAbsListView(mListView);//用于传入list布局
        //Euclid ListView滑动和点击进去其他页面特效，通过SwingLeftInAnimationAdapter
        //实现每一个item点击和滑动进入详细页面的动画效果，提供getViewAnimator()方法。
        mListViewAnimator = mListViewAnimationAdapter.getViewAnimator();
        if (mListViewAnimator != null) {
            //反弹回屏幕正中这部分的动画时间间隔，单位毫秒
            mListViewAnimator.setAnimationDurationMillis(getAnimationDurationCloseProfileDetails());
            //禁用视图动画。使用重新启用它们；默认是启用的
            mListViewAnimator.disableAnimations();
        }

        //设置适配器
        mListView.setAdapter(mListViewAnimationAdapter);

        //设置item点击效果
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mState = EuclidState.Opening;

                showProfileDetails((Map<String, Object>) parent.getItemAtPosition(position), view);
            }
        });
    }

    /**
     * This method counts delay before profile toolbar and profile details start their transition
     * animations, depending on clicked list item on-screen position.
     * 此方法计算配置文件工具栏和概要文件详细信息开始其转换动画之前的延迟，
     * 具体取决于单击的列表项在屏幕上的位置。
     *
     * @param item - data from adapter, that will be set into overlay view.来自适配器的数据，将被设置为覆盖视图
     * @param view - clicked view.
     */
    private void showProfileDetails(Map<String, Object> item, final View view) {
        mListView.setEnabled(false);//设置成true时，相当于激活，状态不再是死的，
        // 而是会对触摸或者点击产生反应，并且可以响应一些触发事件。
        // 而设置成false时，按钮是灰色的，无论是否可点击（即使将setClickable()设置成true），都无法响应任何触发事件。


        // Math.abs() 返回参数的绝对值。参数可以是 int, float, long, double, short, byte类型。
        int profileDetailsAnimationDelay = getMaxDelayShowDetailsAnimation() * Math.abs(view.getTop())//距离父容器的距离
                / sScreenWidth;

        //添加覆盖视图
        addOverlayListItem(item, view);
        //
        startRevealAnimation(profileDetailsAnimationDelay);
        animateOpenProfileDetails(profileDetailsAnimationDelay);
    }

    /**
     * This method inflates a clone of clicked view directly above it. Sets data into it.
     *此方法将在其正上方放大单击视图的克隆。在其中设置数据。
     *
     * @param item - data from adapter, that will be set into overlay view.来自适配器的数据，将被设置为覆盖视图
     * @param view - clicked view.点击的视图
     */
    private void addOverlayListItem(Map<String, Object> item, View view) {
        if (mOverlayListItemView == null) {
            mOverlayListItemView = getLayoutInflater().inflate(R.layout.overlay_list_item, mWrapper, false);
        } else {
            mWrapper.removeView(mOverlayListItemView);
        }

        mOverlayListItemView.findViewById(R.id.view_avatar_overlay).setBackground(sOverlayShape);//设置控件形状显示在list中


        Picasso.with(EuclidActivity.this)
                .load((Integer) item.get(EuclidListAdapter.KEY_AVATAR))//获取到的是resourceId
                .resize(sScreenWidth, sProfileImageHeight)
                .centerCrop()//裁剪
                .placeholder(R.color.blue)//未加载出来的时候显示蓝色背景
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));//加载进视图控件

        //list item
        // centerCrop()使用了一种裁剪技术去缩放图片，去填充ImageView的界限，然后裁剪掉多余的部分。
        // 使用这个方法会使ImageView会被填充的很合适，但是图片可能不能完全显示出来。
        Picasso.with(EuclidActivity.this).load((Integer) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight)
                .centerCrop()//进行裁剪
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar));//

        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_name)).setText((String) item.get(EuclidListAdapter.KEY_NAME));
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_description)).setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_SHORT));
        //name details_full
        setProfileDetailsInfo(item);

        //
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,//宽
                ViewGroup.LayoutParams.WRAP_CONTENT);//高
        params.topMargin = view.getTop() + mToolbar.getHeight();
        params.bottomMargin = -(view.getBottom() - mListView.getHeight());
        mWrapper.addView(mOverlayListItemView, params);
        mToolbar.bringToFront();
    }

    /**
     * This method sets data of the clicked list item to profile details view.
     *单击此项的数据集详细信息列表。
     * @param item - data from adapter, that will be set into overlay view.
     */
    private void setProfileDetailsInfo(Map<String, Object> item) {
        mTextViewProfileName.setText((String) item.get(EuclidListAdapter.KEY_NAME));
        mTextViewProfileDescription.setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_FULL));
    }

    /**
     * This method starts circle reveal animation on list item overlay view, to show full-sized
     * avatar image underneath it. And starts transition animation to position clicked list item
     * under the toolbar.
     * 此方法在列表项覆盖视图上启动圆形显示动画，以在其下方显示完整大小的头像图像。
     * 并开始转换动画以将单击的列表项放置在工具栏下。
     *
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     */
    private void startRevealAnimation(final int profileDetailsAnimationDelay) {
        // 子线程是不能进行 UI 操作的，很多场景下，一些操作需要延迟执行，这些都可以通过 Handler 来解决。
        // 但是写 Handler 太麻烦了，一不小心又很容易写出内存泄漏的代码来，所以为了方便，
        // 就会用 View.post() or View.postDelay() 来代替 Handler 使用。
        mOverlayListItemView.post(new Runnable() {
            @Override
            public void run() {
                getAvatarRevealAnimator().start();
                getAvatarShowAnimator(profileDetailsAnimationDelay).start();
            }
        });
    }

    /**
     * This method creates and setups circle reveal animation on list item overlay view.
     * 此方法在列表项覆盖视图上创建和设置圆形显示动画。
     * @return - animator object that starts circle reveal animation.
     */
    private SupportAnimator getAvatarRevealAnimator() {
        final LinearLayout mWrapperListItemReveal = (LinearLayout) mOverlayListItemView.findViewById(R.id.wrapper_list_item_reveal);
        //覆盖视图的大小
        int finalRadius = Math.max(mOverlayListItemView.getWidth(), mOverlayListItemView.getHeight());

        //快速实现圆形缩放动画的api，必须要设置最低的 api 版本是 21
        final SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(
                mWrapperListItemReveal,//view,
                sScreenWidth / 2, sProfileImageHeight / 2,//圆心坐标
                dpToPx(getCircleRadiusDp() * 2),//startRadius,
                finalRadius);//endRadius
        mRevealAnimator.setDuration(getRevealAnimationDuration());
        mRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart() {
                mWrapperListItemReveal.setVisibility(View.VISIBLE);
                mOverlayListItemView.setX(0);
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        return mRevealAnimator;
    }

    /**
     * This method creates transition animation to move clicked list item under the toolbar.
     * 此方法创建transition animation以移动工具栏下单击的列表项。
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     * @return - animator object that starts transition animation.
     */
    private Animator getAvatarShowAnimator(int profileDetailsAnimationDelay) {
        //使用ofFloat实现属性动画 target:动画目标对象
        //property：动画作用的属性，有了这个属性对象， 就可以不用写属性对应的字段值，类似不用写“scale”
        //values：动画取值，如果是一个值则将target开始的状态作为开始值，将values的一个值，作为结束值，如果是两个值则第一个为动画开始值，第二个为动画结束值。
        final Animator mAvatarShowAnimator = ObjectAnimator.ofFloat(mOverlayListItemView,//目标控件
                View.Y,//property 在y轴上移动
                mOverlayListItemView.getTop(),
                mToolbarProfile.getBottom());

        //设置动画播放完成的时间
        mAvatarShowAnimator.setDuration(profileDetailsAnimationDelay + getAnimationDurationShowProfileDetails());
        //设置加速器 此处为减速器
        mAvatarShowAnimator.setInterpolator(new DecelerateInterpolator());

        return mAvatarShowAnimator;
    }

    /**
     * This method starts set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     * 此方法在传递的延迟之后立即启动一组转换动画，
     * 这些动画显示配置文件工具栏和配置文件详细信息视图。
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details
     *                                     start their transition animations.
     *                                     在配置文件工具栏和配置文件详细信息开始其转换动画之前延迟。
     */
    private void animateOpenProfileDetails(int profileDetailsAnimationDelay) {
        createOpenProfileButtonAnimation();
        getOpenProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }

    /**
     * This method creates if needed the set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     *如果需要，此方法会在传递的延迟之后创建一组变换动画，这些动画显示纵断面工具栏和纵断面详细信息视图。
     * @param profileDetailsAnimationDelay- delay before profile toolbar and profile details
     *                                      start their transition animations.
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getOpenProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if (mOpenProfileAnimatorSet == null) {
            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(getOpenProfileToolbarAnimator());
            profileAnimators.add(getOpenProfileDetailsAnimator());

            mOpenProfileAnimatorSet = new AnimatorSet();
            //同时播放
            mOpenProfileAnimatorSet.playTogether(profileAnimators);
            //动画持续时间显示个人资料细节
            mOpenProfileAnimatorSet.setDuration(getAnimationDurationShowProfileDetails());//500
        }
        mOpenProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);//
        mOpenProfileAnimatorSet.setInterpolator(new DecelerateInterpolator());//加速器，此处为减速
        return mOpenProfileAnimatorSet;
    }

    /**
     * This method, if needed, creates and setups animation of scaling button from 0 to 1.
     * 小图标的效果 从0到1 从无到实际大小
     */
    private void createOpenProfileButtonAnimation() {
        if (mProfileButtonShowAnimation == null) {
            mProfileButtonShowAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_button_scale);
            mProfileButtonShowAnimation.setDuration(getAnimationDurationShowProfileButton());
            mProfileButtonShowAnimation.setInterpolator(new AccelerateInterpolator());
            mProfileButtonShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mButtonProfile.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * This method creates and setups animator which shows profile toolbar.
     * 此方法创建并设置显示配置文件工具栏的animator。
     * 点进去的时候
     *
     * @return - animator object.
     */
    private Animator getOpenProfileToolbarAnimator() {
        //在y轴上移动
        Animator mOpenProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.Y,
                -mToolbarProfile.getHeight(), 0);
        mOpenProfileToolbarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbarProfile.setX(0);
                mToolbarProfile.bringToFront();//Android 中的ViewGroup是通过一个Array来保存其Children，
                // 当调用某个childView的bringToFront时，是将该childView放在其Parent的Array数组的最后，
                // ViewGroup的dispatchDraw在draw时是按照Array从前往后依次调用drawChild的，这样最后一个childView就在最前面了。
                // 意思就是说把当前childView提取到最前面
                mToolbarProfile.setVisibility(View.VISIBLE);
                mProfileDetails.setX(0);
                mProfileDetails.bringToFront();
                mProfileDetails.setVisibility(View.VISIBLE);


                mButtonProfile.setX(mInitialProfileButtonX);
                mButtonProfile.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mButtonProfile.startAnimation(mProfileButtonShowAnimation);
                mState = EuclidState.Opened;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return mOpenProfileToolbarAnimator;
    }

    /**
     * This method creates animator which shows profile details.
     * 此方法创建显示轮廓细节的animator。点击之后的上滑
     * @return - animator object.
     */
    private Animator getOpenProfileDetailsAnimator() {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y,
                getResources().getDisplayMetrics().heightPixels,//DisplayMetircs 类获取分辨率
                getResources().getDimensionPixelSize(R.dimen.height_profile_picture_with_toolbar));
        return mOpenProfileDetailsAnimator;
    }

    /**
     * This method starts set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views.
     * 此方法启动一组转换动画，这组动画隐藏配置文件工具栏此方法启动一组转换动画，
     * 其中隐藏配置文件工具栏、配置文件头像和配置文件详细信息视图。
     */
    private void animateCloseProfileDetails() {
        mState = EuclidState.Closing;
        //播放关闭的动画
        getCloseProfileAnimatorSet().start();
    }

    /**
     * This method creates if needed the set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views. Also it calls notifyDataSetChanged() on the ListView's adapter,
     * so it starts slide-in left animation on list items.
     *此方法根据需要创建一组过渡动画，其中隐藏配置文件工具栏、概要文件头像和概要文件详细信息视图。
     * 它还在ListView的适配器上调用notifyDataSetChanged（），因此它开始在列表项上左滑入动画。
     *
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getCloseProfileAnimatorSet() {
        //mCloseProfileAnimatorSet为空的时候进行初始化，不为空的时候直接返回
        if (mCloseProfileAnimatorSet == null) {
            //加上各个组件的效果

            //标题栏（不设置延迟）
            Animator profileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.X,
                    0, mToolbarProfile.getWidth());

            //图象 设置延迟
            Animator profilePhotoAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.X,
                    0, mOverlayListItemView.getWidth());
            profilePhotoAnimator.setStartDelay(getStepDelayHideDetailsAnimation());

            //小图标 设置延迟
            Animator profileButtonAnimator = ObjectAnimator.ofFloat(mButtonProfile, View.X,
                    mInitialProfileButtonX, mOverlayListItemView.getWidth() + mInitialProfileButtonX);
            profileButtonAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);

            //详细信息 设置延迟
            Animator profileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.X,
                    0, mToolbarProfile.getWidth());
            profileDetailsAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);

            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(profileToolbarAnimator);
            profileAnimators.add(profilePhotoAnimator);
            profileAnimators.add(profileButtonAnimator);
            profileAnimators.add(profileDetailsAnimator);

            mCloseProfileAnimatorSet = new AnimatorSet();
            mCloseProfileAnimatorSet.playTogether(profileAnimators);//需要设置动画的控件
            //500
            mCloseProfileAnimatorSet.setDuration(getAnimationDurationCloseProfileDetails());
            //加速器
            mCloseProfileAnimatorSet.setInterpolator(new AccelerateInterpolator());
            //为了实现左滑
            mCloseProfileAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mListViewAnimator != null) {
                        mListViewAnimator.reset();
                        mListViewAnimationAdapter.notifyDataSetChanged();
                    }
                }

                //将组件设置为不可见
                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolbarProfile.setVisibility(View.INVISIBLE);
                    mButtonProfile.setVisibility(View.INVISIBLE);
                    mProfileDetails.setVisibility(View.INVISIBLE);

                    mListView.setEnabled(true);//设置成true时，相当于激活，状态不再是死的，
                    // 而是会对触摸或者点击产生反应，并且可以响应一些触发事件。
                    // 而设置成false时，按钮是灰色的，无论是否可点击（即使将setClickable()设置成true），都无法响应任何触发事件。

                    //禁用视图动画。使用重新启用它们；默认是启用的
                    mListViewAnimator.disableAnimations();

                    mState = EuclidState.Closed;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return mCloseProfileAnimatorSet;
    }

    /**
     * This method creates a view with empty/transparent circle in it's center. This view is used
     * to cover the profile avatar.
     * 此方法创建一个中心为空/透明圆的视图。此视图用于覆盖配置文件化身。设置给sOverlayShape
     *
     * @return - ShapeDrawable object.
     */
    private ShapeDrawable buildAvatarCircleOverlay() {
        //通过颜色来构造的图形，既可以是纯色的图形，也可以是渐变的图形，理解为图形绘制原始形状的可绘制对象。
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null,
                new RectF(
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(getResources().getColor(R.color.yellow));

        return overlay;
    }

    //大小转换为像素 dp转换为px
    public int dpToPx(int dp) {
        //DisplayMetrics中的density = dpi / 160
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    //点击手机返回按钮的时候，启用的效果
    @Override
    public void onBackPressed() {
        if (getState() == EuclidState.Opened) {
            animateCloseProfileDetails();
        } else if (getState() == EuclidState.Closed) {
            super.onBackPressed();
        }
    }

    /**
     * To use EuclidActivity class, at least this method must be implemented, with your own data.
     *准备数据 准备EnclidListAdapter 这个方法需要在main中实现
     * @return - adapter with data. Check {@link com.yalantis.euclid.library.EuclidListAdapter}
     */
    protected abstract BaseAdapter getAdapter(List<Course> courses);

    /**
     * Returns current profile details state.
     *详情界面的状态
     * @return - {@link com.yalantis.euclid.library.EuclidState}
     */
    public EuclidState getState() {
        return mState;
    }

    /**
     * Duration of circle reveal animation.
     *圆显示动画的持续时间。
     * @return - duration in milliseconds.
     */
    protected int getRevealAnimationDuration() {
        return REVEAL_ANIMATION_DURATION;
    }

    /**
     * Maximum delay between list item click and start of profile toolbar and profile details
     * transition animations. If clicked list item was positioned right at the top - we start
     * profile toolbar and profile details transition animations immediately, otherwise increase
     * start delay up to this value.
     * 列表项单击和配置文件工具栏开始和配置文件详细信息转换动画之间的最大延迟。
     * 如果单击的列表项位于顶部-我们会立即启动配置文件工具栏和配置文件详细信息转换动画，
     * 否则将启动延迟增加到该值。
     *
     * @return - duration in milliseconds.
     */
    protected int getMaxDelayShowDetailsAnimation() {
        return MAX_DELAY_SHOW_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile toolbar and profile details transition animations.
     *显示详细信息的时长
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationShowProfileDetails() {
        return ANIMATION_DURATION_SHOW_PROFILE_DETAILS;
    }

    /**
     * Duration of delay between profile toolbar, profile avatar and profile details close animations.
     * 配置文件工具栏、配置文件头像和配置文件详细信息关闭动画之间的延迟时间。
     * @return - duration in milliseconds.
     * 持续时间（毫秒）。
     */
    protected int getStepDelayHideDetailsAnimation() {
        return STEP_DELAY_HIDE_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile details close animation.
     *轮廓细节关闭动画的持续时间。
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationCloseProfileDetails() {
        return ANIMATION_DURATION_CLOSE_PROFILE_DETAILS;
    }

    //显示小图标的时长
    protected int getAnimationDurationShowProfileButton() {
        return ANIMATION_DURATION_SHOW_PROFILE_BUTTON;
    }

    /**
     * Radius of empty circle inside the avatar overlay.
     *圆形半径
     * @return - size dp.
     */
    protected int getCircleRadiusDp() {
        return CIRCLE_RADIUS_DP;
    }

}
