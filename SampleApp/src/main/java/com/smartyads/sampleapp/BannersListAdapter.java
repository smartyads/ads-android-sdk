package com.smartyads.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import static com.smartyads.sampleapp.BannerActivity.BANNER_LAYOUT_INTENT_KEY;

class BannersListAdapter extends ArrayAdapter<Banner> implements AdapterView.OnItemClickListener{

    BannersListAdapter(Context context, int resource, Banner[] objects) {
        super(context, resource, objects);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Banner banner = getItem(position);
        Context context = getContext();
        Intent showBannerActivity = new Intent(context, bannerActivityClassFactory(banner));
        showBannerActivity.putExtra(BANNER_LAYOUT_INTENT_KEY, banner.getId());
        context.startActivity(showBannerActivity);
    }

    private Class<? extends Activity> bannerActivityClassFactory(Banner banner){
        switch (banner){
            case FULLSCREEN_BANNER:
            case VAST:
                return FullscreenAdActivity.class;
            default:
                return BannerActivity.class;
        }
    }

}
