package com.chattthedev.foodscentral.Viewholders;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chattthedev.foodscentral.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RestaurantViewholder extends RecyclerView.ViewHolder {

    public TextView servicetime, resname, resaddress;
    public RatingBar ratingBar;
    public ImageView resimage;
    public FloatingActionButton floatingActionButton;
    public CardView cardView;

    public RestaurantViewholder(@NonNull View itemView) {
        super(itemView);

        servicetime = itemView.findViewById(R.id.servicetime);
        resimage = itemView.findViewById(R.id.restaurantimage);
        resname = itemView.findViewById(R.id.restaurantname);
        resaddress = itemView.findViewById(R.id.restaurantaddress);
        ratingBar = itemView.findViewById(R.id.resstar);
        floatingActionButton = itemView.findViewById(R.id.floatingActionButton);
        cardView = itemView.findViewById(R.id.newcard);
    }
}
