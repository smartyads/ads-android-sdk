package com.smartyads.sampleapp;

import android.support.test.espresso.IdlingResource;
import android.view.View;

class ViewIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private View view;

    ViewIdlingResource(View bannerContainer) {
        this.view = bannerContainer;
    }

    @Override
    public String getName() {
        return view.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        if (view.getVisibility() == View.VISIBLE
                || view.getVisibility() == View.GONE) {
            resourceCallback.onTransitionToIdle();
            return true;
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}