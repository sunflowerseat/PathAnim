package com.fancy.pathanim;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancy.path_anim_lib.AnimatedSvgView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class MainActivity extends AppCompatActivity {

    private AnimatedSvgView mAnimatedSvgView;
    private RelativeLayout rl_background;
    private RelativeLayout big_background;
    private TextView versionCode;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        //路径动画的View
        mAnimatedSvgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        //需要整体上移的RelativeLayout
        rl_background = (RelativeLayout) findViewById(R.id.rl_background);
        //需要变换形状的RelativeLayout
        big_background = (RelativeLayout) findViewById(R.id.big_background);
        //版本号文字
        versionCode = (TextView) findViewById(R.id.versionCode);
        versionCode.setAlpha(0);
        //logo下方文字
        name = (TextView) findViewById(R.id.name);
        name.setAlpha(0);
        preAnim(rl_background);
        mAnimatedSvgView.getLayoutParams().width = getScreenWidth(this) / 2;
        mAnimatedSvgView.getLayoutParams().height = getScreenWidth(this) / 2;


        mAnimatedSvgView.setGlyphStrings(AnimPath.ANIM_PATH);
        //Path填充颜色
        mAnimatedSvgView.setFillPaints(255,255,255,255);
        //设置跑动光线的颜色
        mAnimatedSvgView.setTraceColors(255,255,255,255);
        //设置轮廓颜色
        mAnimatedSvgView.setTraceResidueColors(255,255,255,255);

        mAnimatedSvgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_FILL_STARTED) {

                    AnimatorSet set = new AnimatorSet();
                    Interpolator interpolator = new DecelerateInterpolator();
                    ObjectAnimator a1 = ObjectAnimator.ofFloat(mAnimatedSvgView, "translationY", 0);
                    a1.setInterpolator(interpolator);
                    set.playTogether(a1);
                    set.start();
                }
            }
        });
    }

    public void preAnim(final View v) {
        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        drawable.setCornerRadius(0);
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "scaleX", new float[]{1f,1f});
        anim.setDuration(1);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                start(v,700);
            }
        });
        anim.start();
    }

    public void endAnim() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator a2 = ObjectAnimator.ofFloat(big_background, "y", big_background.getY(),big_background.getY()/8);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(versionCode, "alpha", 0f,1f);
        ObjectAnimator a4 = ObjectAnimator.ofFloat(name, "alpha", 0f,1f);
        set.playTogether(a2, a3, a4);
        set.setDuration(700);
        set.start();
        mAnimatedSvgView.start();
    }

    /**
     * 矩形变圆角动画
     */
    public void start(final View v,long duration) {
        //需要的参数 drawable对象
        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(v.getBackground(), "cornerRadius", new float[]{0, v.getWidth() / 2});
        cornerAnimation.setDuration(duration);
        final ObjectAnimator heightAnimation = ObjectAnimator.ofInt(v, "xxx", new int[]{v.getHeight(), v.getWidth() / 2});
        heightAnimation.setDuration(duration);
        heightAnimation.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                v.getLayoutParams().height = (int) heightAnimation.getAnimatedValue();
                v.requestLayout();
            }
        });
        ObjectAnimator animator = ObjectAnimator.ofInt(v, "xx", v.getWidth(), v.getWidth() / 2).setDuration(duration);
        animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                v.getLayoutParams().width = value;
                v.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(cornerAnimation).with(heightAnimation).with(animator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endAnim();
            }
        });
        animatorSet.start();
    }

    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
}
