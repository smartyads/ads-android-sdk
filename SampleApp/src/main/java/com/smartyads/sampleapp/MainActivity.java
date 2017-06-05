package com.smartyads.sampleapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;

    private String[] requiredPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermission(getNotGrantedPermissions(requiredPermissions));

        ListView bannersList = (ListView) findViewById(R.id.bannersList);
        BannersListAdapter adapter = new BannersListAdapter(
                this,
                android.R.layout.simple_list_item_1,
                Banner.values()
        );
        bannersList.setAdapter(adapter);
        bannersList.setOnItemClickListener(adapter);
    }

    private List<String> getNotGrantedPermissions(String[] requiredPermissions){
        List<String> notGranted = new ArrayList<>();
        for (String requiredPermission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, requiredPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(requiredPermission);
            }
        }
        return notGranted;
    }

    private void askForPermission(List<String> notGranted){
        if(notGranted.size() == 0){
            return;
        }
        try {
            ActivityCompat.requestPermissions(this, notGranted.toArray(new String[0]), REQUEST_CODE_PERMISSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
