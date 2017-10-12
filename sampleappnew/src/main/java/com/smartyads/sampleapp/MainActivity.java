package com.smartyads.sampleapp;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.smartyads.Ads;
import com.smartyads.adcontainer.context.PlacementType;

public class MainActivity extends AppCompatActivity {

  private static final int LOCATIONS_PERMISSION_REQUEST = 138;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // init ads sdk
    Ads.init(getApplicationContext());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[] {
          Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
      }, LOCATIONS_PERMISSION_REQUEST);
    }

    AdAdapter adapter = new AdAdapter(new onAdTypeClicked() { // Process selected ad
      @Override public void onClicked(PlacementType placementType) {
        AdActivity.start(MainActivity.this, placementType);
      }
    });

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ads_type_recycler);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private interface onAdTypeClicked {

    void onClicked(PlacementType placementType);
  }

  private static class AdAdapter extends RecyclerView.Adapter<AdAdapter.SingleLineViewHolder> {

    private final onAdTypeClicked onAdTypeClickedListener;

    private AdAdapter(onAdTypeClicked onAdTypeClickedListener) {
      this.onAdTypeClickedListener = onAdTypeClickedListener;
    }

    @Override public SingleLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, parent, false);
      return new SingleLineViewHolder(view);
    }

    @Override public void onBindViewHolder(final SingleLineViewHolder holder, final int position) {
      holder.mTitle.setText(PlacementType.values()[position].toString());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (onAdTypeClickedListener != null) {
            onAdTypeClickedListener.onClicked(PlacementType.values()[holder.getAdapterPosition()]);
          }
        }
      });
    }

    @Override public int getItemCount() {
      return PlacementType.values().length;
    }

    class SingleLineViewHolder extends RecyclerView.ViewHolder {
      final TextView mTitle;

      SingleLineViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title);
      }
    }
  }
}
