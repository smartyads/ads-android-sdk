package com.smartyads.sampleapp;

import android.os.Bundle;
import android.view.ViewGroup;

import com.smartyads.adcontainer.BannerContainer;
import com.smartyads.adcontainer.listener.BannerOnLoadListener;

public class BannerActivity extends AbstractAdActivity {

    private BannerContainer bannerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_banner);
        bannerContainer = inflateBannerContainer(bannerFromIntent());
        super.onCreate(savedInstanceState);

        onAdLoadStarted();
        bannerContainer.loadAd(createBannerOnLoadListener());
    }

    public BannerContainer inflateBannerContainer(Banner banner){
        ViewGroup rootView = (ViewGroup) findViewById(R.id.root);
        getLayoutInflater().inflate(banner.layout, rootView);
        BannerContainer viewById = (BannerContainer) findViewById(R.id.banner_container);
        viewById.setTag(R.id.banner_container);
        return viewById;
    }

    @Override
    BannerOnLoadListener createBannerOnLoadListener(){
        final BannerOnLoadListener bannerOnLoadListener = super.createBannerOnLoadListener();
        return new BannerOnLoadListener() {
            @Override
            public void onSuccess() {
                bannerOnLoadListener.onSuccess();
                bannerContainer.showAd();
            }

            @Override
            public void onFailure(Exception e) {
                bannerOnLoadListener.onFailure(e);
            }
        };
    }
}
