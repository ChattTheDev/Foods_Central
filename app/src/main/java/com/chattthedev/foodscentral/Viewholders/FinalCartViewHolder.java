package com.chattthedev.foodscentral.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chattthedev.foodscentral.R;

public class FinalCartViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_cart_name, txt_resname, txt_halforfull;
    public ImageView delete, menuimage;
    public ElegantNumberButton elegantNumberButton;
    public FinalCartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cartprodname);
        txt_halforfull = itemView.findViewById(R.id.halforfull);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        //elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.elegentbutt);
        txt_resname = itemView.findViewById(R.id.resname);
        menuimage = itemView.findViewById(R.id.menuimagecart);

    }
}
