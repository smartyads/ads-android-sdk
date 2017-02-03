package com.smartyads.sampleapp;

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
        Intent showBannerActivity = new Intent(getContext(), BannerActivity.class);
        Banner banner = getItem(position);
        showBannerActivity.putExtra(BANNER_LAYOUT_INTENT_KEY, banner.layout);
        getContext().startActivity(showBannerActivity);
    }
}
