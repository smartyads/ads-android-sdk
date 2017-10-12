package com.smartyads.sampleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.smartyads.adcontainer.NativeAdContainer;
import com.smartyads.adcontainer.listener.NativeOnLoadListener;

/**
 * Created by alex on 10/3/17.
 */

class NativeAdapter extends RecyclerView.Adapter<NativeAdapter.ItemHolder> {

  private NativeOnLoadListener statusListener;

  NativeAdapter(NativeOnLoadListener statusListener) {
    this.statusListener = statusListener;
    statusListener.onSuccess();
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_native_list_item, parent, false);
    return new ItemHolder(view);
  }

  @Override public void onBindViewHolder(ItemHolder holder, int position) {
    if (position % 10 == 0) { // ShowAd
      holder.clear();
      NativeAdContainer nat = NativeAdContainer.Builder(holder.itemView.getContext(),
          holder.itemView.getContext().getString(R.string.native_placement_id))
          .setTitleView(holder.title)
          .setDescriptionView(holder.description)
          .setSponsoredTextView(holder.sponsoredText)
          .setPriceView(holder.price)
          .setImageView(holder.image)
          .setCtaView(holder.actionButton)
          .build();
      nat.loadAd(statusListener);
    } else { // Show content
      holder.title.setText("Gift box");
      holder.price.setText("$" + (1000 - position * 3));
      holder.description.setText("So nice thing. So many sweets");
      holder.sponsoredText.setText("Amazon shop");
      holder.actionButton.setText("Buy");
      holder.image.setImageResource(R.drawable.box_with_items);
    }
  }

  @Override public int getItemCount() {
    return 55;
  }

  class ItemHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final TextView description;
    final TextView sponsoredText;
    final TextView price;
    final ImageView image;
    final Button actionButton;

    ItemHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.native_title);
      description = itemView.findViewById(R.id.native_description);
      sponsoredText = itemView.findViewById(R.id.native_sponsor);
      price = itemView.findViewById(R.id.native_price);
      image = itemView.findViewById(R.id.native_image);
      actionButton = itemView.findViewById(R.id.native_button);
    }

    private void clear() {
      title.setText("");
      description.setText("");
      sponsoredText.setText("");
      actionButton.setText("Buy");
      image.setImageResource(android.R.color.transparent);
    }
  }
}
