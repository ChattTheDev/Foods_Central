package com.chattthedev.foodscentral.Viewholders;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chattthedev.foodscentral.Activities.CartPage;
import com.chattthedev.foodscentral.Activities.Main_Dashboard;
import com.chattthedev.foodscentral.Database.DBHelper;
import com.chattthedev.foodscentral.Interface.ItemClickListener;
import com.chattthedev.foodscentral.Models.Order;
import com.chattthedev.foodscentral.R;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name, txt_price, txt_halforfull;
    public ImageView delete;
    //public ElegantNumberButton elegantNumberButton;
    public Button minus, plus;
    TextView counts;
    Context context;


    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        txt_cart_name = (TextView) itemView.findViewById(R.id.cartprodname);
        txt_halforfull = itemView.findViewById(R.id.halforfull);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        counts = itemView.findViewById(R.id.prodcounter);
        minus = itemView.findViewById(R.id.buttminusitempage);
        plus = itemView.findViewById(R.id.buttplusitempage);
        //elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.elegentbutt);

    }

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    @Override
    public void onClick(View v) {

    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    int total1 = 0;
    int total2 = 0;
    int total3 = 0;
    int a = 0;
    int b = 0;
    int c = 0;
    int f1 = 0;
    int f2 = 0;
    int f3 = 0;
    int fintotal = 0;
    int count = 0;

    private List<Order> listData = new ArrayList<>();
    private CartPage cartPage;

    public CartAdapter(List<Order> listData, CartPage cartPage) {
        this.listData = listData;
        this.cartPage = cartPage;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cartPage);
        View itemView = inflater.inflate(R.layout.singleitemforcartpage, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        holder.txt_cart_name.setText(listData.get(position).getProductName());
        holder.counts.setText(listData.get(position).getQuantity());
        if (!listData.get(position).getHalf().equals("")) {
            holder.txt_halforfull.setText(listData.get(position).getHalf() + " : ₹" + listData.get(position).getHalfprice());
            total3 = (Integer.parseInt(listData.get(position).getHalfprice()) * Integer.parseInt(holder.counts.getText().toString()));

        } else if (!listData.get(position).getFull().equals("")) {
            holder.txt_halforfull.setText(listData.get(position).getFull() + " : ₹" + listData.get(position).getFullprice());
            total2 += (Integer.parseInt(listData.get(position).getFullprice()) * Integer.parseInt(holder.counts.getText().toString()));

        } else {
            holder.txt_halforfull.setText("₹" + listData.get(position).getPrice());
            total1 += (Integer.parseInt(listData.get(position).getPrice()) * Integer.parseInt(holder.counts.getText().toString()));
        }
        fintotal = total1 + total2 + total3;
        cartPage.carttotalprice.setText(String.valueOf(fintotal));


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(holder.counts.getText().toString());
                count = count + 1;
                holder.counts.setText("" + count);
                Order order = listData.get(position);
                order.setQuantity(holder.counts.getText().toString());
                new DBHelper(cartPage).updatecart(order);

                int total = 0;
                List<Order> orders = new DBHelper(cartPage).getCarts();
                for (Order item : orders){
                    if (!item.getHalf().equals("")) {
                        total1 += Integer.parseInt(item.getHalfprice()) * Integer.parseInt(item.getQuantity());

                    } else if (!item.getFull().equals("")) {
                        total2 += Integer.parseInt(item.getFullprice()) * Integer.parseInt(item.getQuantity());

                    } else {
                        total3 += Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity());
                    }

                    fintotal = total1 + total2 + total3;

                }

                cartPage.carttotalprice.setText(String.valueOf(fintotal));


//                int fintotalfin = 0;
//                fintotalfin = f1 + f2 + f3;
//                fintotal = fintotal + fintotalfin;
//                cartPage.carttotalprice.setText(String.valueOf(fintotal));
////                if (!cartPage.carttotalprice.getText().toString().equals("")) {
////                    cartPage.carttotalprice.setText(String.valueOf(fintotal));
////                } else {
////                    cartPage.carttotalprice.setText(String.valueOf(fintotalfin));
////                }


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(holder.counts.getText().toString());
                count = count - 1;
                if (count == 0) {
                    holder.counts.setText("0");
                } else if (count < 0) {
                    Toast.makeText(cartPage, "Please Check Details", Toast.LENGTH_SHORT).show();

                } else {
                    holder.counts.setText("" + count);
                }


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(cartPage);
//
//
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.singleitemdialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                final Button b1 = (Button) dialog.findViewById(R.id.yes);
                final Button b2 = (Button) dialog.findViewById(R.id.no);
                dialog.show();
                dialog.setCancelable(false);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Order order = listData.get(position);
                        int bbb = order.getID();
                        new DBHelper(cartPage).removeFromCart(bbb);


                        Intent intent = new Intent(cartPage, Main_Dashboard.class);
                        cartPage.startActivity(intent);

                        Toast.makeText(cartPage, "Item Deleted...", Toast.LENGTH_SHORT).show();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


}
