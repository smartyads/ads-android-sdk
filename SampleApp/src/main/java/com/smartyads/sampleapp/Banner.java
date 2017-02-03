package com.smartyads.sampleapp;

import android.support.annotation.IdRes;

enum Banner {
    MEDIUM_RECTANGLE(R.layout.view_example_medium_banner){
        @Override
        public String toString() {
            return "Medium Rectangle 300x250";
        }
    },
    BANNER(R.layout.view_example_banner){
        @Override
        public String toString() {
            return "Standard Banner 320x50";
        }
    },
    LARGE_BANNER(R.layout.view_example_large_banner){
        @Override
        public String toString() {
            return " Large Banner 320x100";
        }
    },
    FULL_BANNER(R.layout.view_example_full_banner){
        @Override
        public String toString() {
            return "Full-Size Banner(tablet) 468x60";
        }
    },
    LEADERBOARD(R.layout.view_example_leaderboard_banner){
        @Override
        public String toString() {
            return "Leaderboard(tablet) 728x90";
        }
    };

    public final @IdRes int layout;

    Banner(int layout) {
        this.layout = layout;
    }
}
