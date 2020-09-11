package com.chattthedev.foodscentral.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chattthedev.foodscentral.Database.DBHelper;
import com.chattthedev.foodscentral.Models.MenuModel;
import com.chattthedev.foodscentral.Models.ModelforCart;
import com.chattthedev.foodscentral.Models.Order;
import com.chattthedev.foodscentral.Models.Request;
import com.chattthedev.foodscentral.R;
import com.chattthedev.foodscentral.Viewholders.CartAdapter;
import com.chattthedev.foodscentral.Viewholders.FinalCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends AppCompatActivity {
    RecyclerView cartrecycler;
    public TextView carttotalprice;
    Button checkout;
    FirebaseRecyclerOptions<ModelforCart> cartoptions;
    FirebaseRecyclerAdapter<ModelforCart, FinalCartViewHolder> adapterforcart;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String user;
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
    List<Order> cart = new ArrayList<>();
    List<Order> cartorder = new ArrayList<>();
    CartAdapter adapter;
    DatabaseReference ordersref;
    List<MenuModel> stringList = new ArrayList<>();
    String cusname, cusmobile, cusaddress, cusemail;
    MenuModel menuModel, menuModel1;
    String finalprice, itemcat, itemlargesizename, itemname, itemsmallsizename, largesizeprice, originalprice, productimage, resid, smallsizeprice, status, menuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        cartrecycler = findViewById(R.id.recyclerviewcartpage);
        carttotalprice = findViewById(R.id.totprice);
        checkout = findViewById(R.id.checkoutbutton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartrecycler.setLayoutManager(linearLayoutManager);
        cartrecycler.setHasFixedSize(true);
        newload();

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();
        //loadcartitems();
        //gettinguservalue();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addtoorders();
            }
        });
//        menuModel = new MenuModel();
//        finalprice = menuModel.getFinalprice();
//        itemcat = menuModel.getItemcat();
//        itemlargesizename = menuModel.getItemlargesizename();
//        itemname = menuModel.getItemname();
//        itemsmallsizename = menuModel.getItemsmallsizename();
//        itemlargesizename = menuModel.getItemlargesizename();
//        largesizeprice = menuModel.getLargesizeprice();
//        originalprice = menuModel.getOriginalprice();
//        productimage = menuModel.getProductimage();
//        resid = menuModel.getResid();
//        smallsizeprice = menuModel.getSmallsizeprice();
//        status = menuModel.getStatus();
//        menuid = menuModel.getMenuid();
//        menuModel1 = new MenuModel(finalprice, itemcat, itemlargesizename, itemname, itemsmallsizename, largesizeprice, originalprice, productimage, resid, smallsizeprice, status, menuid);


    }


    private void loadcartitems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Temp_Cart");
        firebaseUser = mAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();

        Query query = reference.child(user);
        cartoptions = new FirebaseRecyclerOptions.Builder<ModelforCart>().setQuery(query, ModelforCart.class).build();
        adapterforcart = new FirebaseRecyclerAdapter<ModelforCart, FinalCartViewHolder>(cartoptions) {
            @NonNull
            @Override
            public FinalCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemforcartpage, parent, false);
                FinalCartViewHolder finalCartViewHolder = new FinalCartViewHolder(view);
                return finalCartViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final FinalCartViewHolder holder, int position, @NonNull final ModelforCart model) {
                holder.elegantNumberButton.setNumber(model.getMenuquant());
                holder.txt_cart_name.setText(model.getMenuname());
                holder.txt_resname.setText("From: " + model.getResname());
                Glide.with(getApplicationContext()).load(model.getMenuimage()).into(holder.menuimage);

                if (!model.getMenuhalf().equals("")) {
                    holder.txt_halforfull.setText(model.getMenuhalf() + " : ₹" + model.getMenuhalfprice());
                    total3 += (Integer.parseInt(model.getMenuhalfprice()) * Integer.parseInt(holder.elegantNumberButton.getNumber()));
//
                } else if (!model.getMenufull().equals("")) {
                    holder.txt_halforfull.setText(model.getMenufull() + " : ₹" + model.getMenufullprice());
                    total2 += (Integer.parseInt(model.getMenufullprice()) * Integer.parseInt(holder.elegantNumberButton.getNumber()));
//                    holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//                        @Override
//                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                            total32 += (Integer.parseInt(model.getMenufullprice()) * newValue);
//                            //carttotalprice.setText(String.valueOf(total32));
//
//                        }
//                    });
                } else {
                    holder.txt_halforfull.setText("₹" + model.getMenuprice());
                    total1 += (Integer.parseInt(model.getMenuprice()) * Integer.parseInt(holder.elegantNumberButton.getNumber()));
                }

                fintotal = total1 + total2 + total3;
                carttotalprice.setText(String.valueOf(fintotal));
                holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        if (!model.getMenuhalf().equals("")) {
                            a = Integer.parseInt(model.getMenuhalfprice());
                            f1 = 0;
                            f1 += newValue * a;

                        } else if (!model.getMenufull().equals("")) {
                            b = Integer.parseInt(model.getMenufullprice());
                            f2 = 0;
                            f2 += newValue * b;

                        } else {
                            c = Integer.parseInt(model.getMenuprice());
                            f3 = 0;
                            f3 += newValue * c;
                        }

                        int fintotalfin = 0;
                        fintotalfin = f1 + f2 + f3;
                        fintotal = fintotal + fintotalfin;
                        if (!carttotalprice.getText().toString().equals("")) {
                            carttotalprice.setText(String.valueOf(fintotal));
                        } else {
                            carttotalprice.setText(String.valueOf(fintotalfin));
                        }


                    }
                });


            }

        };
        adapterforcart.startListening();
        cartrecycler.setAdapter(adapterforcart);

    }

    public void addtoorders() {
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Users");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cusname = snapshot.child(user).child("customerName").getValue(String.class);
                cusmobile = snapshot.child(user).child("customerMobile").getValue(String.class);
                cusemail = snapshot.child(user).child("customeremail").getValue(String.class);
                cusaddress = snapshot.child(user).child("customerFullAddress").getValue(String.class);
                ordersref = FirebaseDatabase.getInstance().getReference().child("Orders");
                cartorder = new DBHelper(CartPage.this).getCarts();


                Request request = new Request(
                        cusmobile,
                        cusname,
                        cusaddress,
                        carttotalprice.getText().toString(),
                        cartorder

                );
                long time = System.currentTimeMillis();
                ordersref.child(String.valueOf(time))
                        .setValue(request);
                new DBHelper(getBaseContext()).cleanCart();
                Intent intent = new Intent(getApplicationContext(), Main_Dashboard.class);
                startActivity(intent);
                Toast.makeText(CartPage.this, "Your  Order has been Confirmed!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void newload() {
        cart = new DBHelper(CartPage.this).getCarts();
        adapter = new CartAdapter(cart, CartPage.this);
        cartrecycler.setAdapter(adapter);

//        int total = 0;
//        for (Order order : cart)
//            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

       // totalprice.setText(String.valueOf(total));

    }

    public void uploadorders() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Temp_Cart");
        stringList = new ArrayList<>();

        reference.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    menuModel1 = i.getValue(MenuModel.class);
                    stringList.add(menuModel1);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public  void up(){


    }

    private void gettinguservalue() {
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Users");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cusname = snapshot.child(user).child("customerName").getValue(String.class);
                cusmobile = snapshot.child(user).child("customerMobile").getValue(String.class);
                cusemail = snapshot.child(user).child("customeremail").getValue(String.class);
                cusaddress = snapshot.child(user).child("customerFullAddress").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}