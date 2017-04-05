package com.smartyads.sampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smartyads.adcontainer.AdContainer;
import com.smartyads.adcontainer.BannerContainer;
import com.smartyads.adcontainer.InterstitialAdContainer;
import com.smartyads.adcontainer.listener.InterstitialListener;

import static android.view.View.GONE;

public class BannerActivity extends AppCompatActivity {

    public static final String BANNER_LAYOUT_INTENT_KEY = "banner";

    private ProgressBar progressBar;
    private AdContainer bannerContainer;
    private TextView timeView;
    private TextView noBannerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        noBannerText = (TextView) findViewById(R.id.no_banner);
        timeView = (TextView) findViewById(R.id.loadTime);

        int bannerTypeId = getIntent().getIntExtra(BANNER_LAYOUT_INTENT_KEY, -1);
        Banner banner = Banner.fromId(bannerTypeId);
        setTitle(banner.toString());
        bannerContainer = createContainer(banner);

        bannerContainer.loadAd(createBannerOnLoadListener());
        startRecordTime();
    }

    private InterstitialListener createBannerOnLoadListener(){
        return new InterstitialListener() {
            @Override
            public void closed() {
                Log.d("[SmartyAds]", "Ad closed!");
                Toast.makeText(BannerActivity.this, "Interstitial closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                dismissProgressBar();
                Log.d("[SmartyAds]", "Banner successfully loaded");
                stopRecordTime();
                timeView.setText("Load time: "+getLoadTime()/1000.0);
                timeView.setVisibility(View.VISIBLE);
                bannerContainer.showAd();
            }

            @Override
            public void onFailure(Exception e) {
                dismissProgressBar();
                Log.d("[SmartyAds]", "Cannot load ad: " + e.getMessage());
                noBannerText.setText("Cannot load ad: " + e.getMessage());
                noBannerText.setVisibility(View.VISIBLE);
            }
        };
    }

    long t1;
    long t2;
    private void startRecordTime(){
        t1 = System.currentTimeMillis();
    }

    private void stopRecordTime(){
        t2 = System.currentTimeMillis();
    }

    private long getLoadTime(){
        return t2 - t1;
    }

    private void dismissProgressBar(){
        if (progressBar != null) {
            progressBar.setVisibility(GONE);
            progressBar.clearAnimation();
        }
    }

    public void recreateActivity(View view){
        recreate();
    }

    @Override
    protected void onDestroy() {
        dismissProgressBar();
        bannerContainer.destroy();
        super.onDestroy();
    }

    private AdContainer createContainer(Banner banner){
        switch (banner){
            case FULLSCREEN_BANNER:
                return new InterstitialAdContainer(this, banner.containerId);
            default:
                return inflateBannerContainer(banner);
        }
    }

    public AdContainer inflateBannerContainer(Banner banner){
        ViewGroup rootView = (ViewGroup) findViewById(R.id.root);
        getLayoutInflater().inflate(banner.layout, rootView);
        BannerContainer viewById = (BannerContainer) findViewById(R.id.banner_container);
        viewById.setTag(R.id.banner_container);
        return viewById;
    }
}
