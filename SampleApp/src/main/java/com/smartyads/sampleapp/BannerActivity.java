package com.smartyads.sampleapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartyads.adcontainer.BannerContainer;
import com.smartyads.adcontainer.BannerOnLoadListener;

public class BannerActivity extends AppCompatActivity {

    public static final String BANNER_LAYOUT_INTENT_KEY = "banner";

    private ProgressDialog progressDialog;
    private BannerContainer bannerContainer;
    private TextView timeView;
    private TextView noBannerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Banner...");

        noBannerText = (TextView) findViewById(R.id.no_banner);
        timeView = (TextView) findViewById(R.id.loadTime);

        inflateBannerContainer();

        BannerOnLoadListener bannerOnLoadListener = createBannerOnLoadListener();
        startRecordTime();
        bannerContainer.loadAd(bannerOnLoadListener);
        progressDialog.show();
    }

    private void inflateBannerContainer(){
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);

        int layoutResource = getIntent().getIntExtra(BANNER_LAYOUT_INTENT_KEY, -1);
        getLayoutInflater().inflate(layoutResource, rootView);

        bannerContainer = (BannerContainer) findViewById(R.id.banner_container);
        bannerContainer.setBackgroundColor(Color.LTGRAY);
    }

    private BannerOnLoadListener createBannerOnLoadListener(){
        return new BannerOnLoadListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                Log.d("[SmartyAds]", "Banner successfully loaded");
                stopRecordTime();
                timeView.setText("Load time: "+getLoadTime()/1000.0);
                timeView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Exception e) {
                dismissProgressDialog();
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

    private void dismissProgressDialog(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void recreateActivity(View view){
        recreate();
    }

    @Override
    protected void onDestroy() {
        bannerContainer.destroyContainer();
        dismissProgressDialog();
        super.onDestroy();
    }
}
