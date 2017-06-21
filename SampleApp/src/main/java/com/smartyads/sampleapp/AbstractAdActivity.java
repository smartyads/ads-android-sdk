package com.smartyads.sampleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartyads.adcontainer.AdContainer;
import com.smartyads.adcontainer.listener.BannerOnLoadListener;
import com.smartyads.info.provided.Gender;
import com.smartyads.info.provided.UserKeywords;
import com.smartyads.info.provided.YearOfBirth;
import com.smartyads.utils.StringUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import static android.view.View.GONE;

public abstract class AbstractAdActivity extends AppCompatActivity {

    public static final String BANNER_LAYOUT_INTENT_KEY = "banner";
    private TextView infoTextView;
    private ProgressBar progressBar;
    private TimeTracker timeTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        infoTextView = (TextView) findViewById(R.id.infoText);

        Banner banner = bannerFromIntent();
        setTitle(banner.toString());
        timeTracker = new TimeTracker();
    }

    void addTargetingData(AdContainer adContainer){
        Random random = new Random();

        Gender[] genders = Gender.values();
        Gender randomGender = genders[random.nextInt(genders.length)];

        int maxAge = 100;
        YearOfBirth yearOfBirth = new YearOfBirth(
                Calendar.getInstance().get(Calendar.YEAR) - maxAge
                        + random.nextInt(maxAge)
        );

        UserKeywords userKeywords = new UserKeywords()
                .addKeywords("interest 1", "interest 2")
                .addKeywords(Collections.singletonList("interest 3"));

        adContainer
                .addTargetingData(randomGender)
                .addTargetingData(yearOfBirth)
                .addTargetingData(userKeywords);
    }

    void onAdLoadStarted(){
        timeTracker.startTimeTraking();
        progressBar.setVisibility(View.VISIBLE);
    }

    Banner bannerFromIntent(){
        int bannerTypeId = getIntent().getIntExtra(BANNER_LAYOUT_INTENT_KEY, -1);
        return Banner.fromId(bannerTypeId);
    }

    BannerOnLoadListener createBannerOnLoadListener(){
        return new BannerOnLoadListener() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(StringUtils.SMARTY_TAG, "Ad successfully loaded");
                infoTextView.setText(
                        getString(R.string.load_time, timeTracker.stopTimeTraking()/1000.0)
                );
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                String failureMessage = getString(R.string.loading_failed, e.getMessage());
                Log.d(StringUtils.SMARTY_TAG, failureMessage);
                infoTextView.setText(failureMessage);
            }
        };
    }

    public void recreateActivity(View view){
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onDestroy() {
        dismissProgressBar();
        super.onDestroy();
    }

    private void dismissProgressBar(){
        if (progressBar != null) {
            progressBar.setVisibility(GONE);
            progressBar.clearAnimation();
        }
    }

    private class TimeTracker{
        long t1;

        void startTimeTraking(){
            t1 = System.currentTimeMillis();
        }

        long stopTimeTraking(){
            return System.currentTimeMillis() - t1;
        }
    }

}
