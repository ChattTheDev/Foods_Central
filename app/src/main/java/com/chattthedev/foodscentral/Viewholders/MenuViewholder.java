package com.chattthedev.foodscentral.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.chattthedev.foodscentral.R;

public class MenuViewholder extends RecyclerView.ViewHolder {
    public ImageView menuimage;
    public TextView menuname, menu_original_price, menu_final_price;
    public LinearLayout l1, vanishedlayout;
    public Button addtocart;
    public TextView ruppe, from;
    public MenuViewholder(@NonNull View itemView) {
        super(itemView);

        menuimage = itemView.findViewById(R.id.menuimageitems);
        menuname = itemView.findViewById(R.id.menuname);
        menu_original_price = itemView.findViewById(R.id.orpriceitem);
        menu_final_price = itemView.findViewById(R.id.finpriceitem);
        l1 = itemView.findViewById(R.id.linearLayout3);
        vanishedlayout = itemView.findViewById(R.id.vanishedlayout);
        addtocart = itemView.findViewById(R.id.addtocart);
        ruppe = itemView.findViewById(R.id.rupeefinal);
        from = itemView.findViewById(R.id.from);
    }
}
