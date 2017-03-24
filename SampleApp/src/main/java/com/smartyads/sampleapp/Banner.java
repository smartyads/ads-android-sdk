package com.smartyads.sampleapp;

import android.support.annotation.LayoutRes;

enum Banner {

    FULLSCREEN_BANNER(0, "3731"){
        @Override
        public String toString() {
            return "Fullscreen banner";
        }
    },
    MEDIUM_RECTANGLE(R.layout.view_example_medium_banner, "3358"){
        @Override
        public String toString() {
            return "Medium Rectangle 300x250";
        }
    },
    BANNER(R.layout.view_example_banner, "3729"){
        @Override
        public String toString() {
            return "Standard Banner 320x50";
        }
    },
    LARGE_BANNER(R.layout.view_example_large_banner, "3730"){
        @Override
        public String toString() {
            return " Large Banner 320x100";
        }
    },
    FULL_BANNER(R.layout.view_example_full_banner, "4405"){
        @Override
        public String toString() {
            return "Full-Size Banner(tablet) 468x60";
        }
    },
    LEADERBOARD(R.layout.view_example_leaderboard_banner, "3732"){
        @Override
        public String toString() {
            return "Leaderboard(tablet) 728x90";
        }
    };

    public final @LayoutRes int layout;
    public final String containerId;

    Banner(int layout, String containerId) {
        this.layout = layout;
        this.containerId = containerId;
    }

    public int getId(){
        return ordinal();
    }

    public static Banner fromId(int id){
        return values()[id];
    }

}
