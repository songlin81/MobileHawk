package com.chubbymobile.wwh.hawk.Landing;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.chubbymobile.wwh.hawk.HawkApplication;
import com.chubbymobile.wwh.hawk.MainActivity;
import com.chubbymobile.wwh.hawk.R;

public class SplashActivity extends FragmentActivity implements Animation.AnimationListener {

    Animation animFadeIn;
    LinearLayout linearLayout;

    private static final int SHOW_TIME_MIN = 4000;
    private long mStartTime;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HawkApplication.SUCCESS:
                    long loadingTime = System.currentTimeMillis() - mStartTime;

                    Bundle b = msg.getData();
                    String version = b.getString("Version");
                    String timeStamp = b.getString("TimeStamp");
                    Log.d("app_version: ", version + " @ "+ timeStamp);

                    if (loadingTime < SHOW_TIME_MIN) {
                        mHandler.postDelayed(goToMainActivity, SHOW_TIME_MIN - loadingTime);
                    } else {
                        mHandler.post(goToMainActivity);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    Runnable goToMainActivity = new Runnable() {
        @Override
        public void run() {
            SplashActivity.this.startActivity(
                    new Intent(SplashActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_slide_in, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mStartTime = System.currentTimeMillis();
        HawkApplication.getInstance().initData(mHandler);
        initAnimation();
    }

    private void initAnimation() {
        findViewById(R.id.ivsplash).getLayoutParams().width = getScreenWidth()/2;

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        animFadeIn.setAnimationListener(this);

        linearLayout = findViewById(R.id.layout_linear);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);
    }

    public int getScreenWidth() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
