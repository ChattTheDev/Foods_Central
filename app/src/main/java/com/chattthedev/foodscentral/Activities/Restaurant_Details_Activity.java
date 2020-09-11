package com.chattthedev.foodscentral.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chattthedev.foodscentral.Database.DBHelper;
import com.chattthedev.foodscentral.Models.MenuModel;
import com.chattthedev.foodscentral.Models.ModelCategories;
import com.chattthedev.foodscentral.Models.Order;
import com.chattthedev.foodscentral.Models.RatingModel;
import com.chattthedev.foodscentral.R;
import com.chattthedev.foodscentral.Viewholders.CatHolder;
import com.chattthedev.foodscentral.Viewholders.MenuViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Restaurant_Details_Activity extends AppCompatActivity {
    ImageView resimagepd;
    TextView resaddfinal;
    CounterFab cartbutt;
    RecyclerView menufetchrecyclerview, filterrecycler;
    FirebaseRecyclerAdapter<MenuModel, MenuViewholder> adapter;
    FirebaseRecyclerOptions<MenuModel> options;
    FirebaseRecyclerAdapter<ModelCategories, CatHolder> adapterfilter;
    FirebaseRecyclerOptions<ModelCategories> optionsfilter;
    DatabaseReference menuref, catref, reference, databaseReferencerate, ratedb;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    int count = 0, bc;
    FirebaseUser firebaseUser;
    String reskey, user, keyfinal, resname, resimagefinal, menuimagecart, menunameforcart, menuquantityforcart, menupriceforcart, menuidforcart, menufullpriceforcart, menuhalfpriceforcart, halfcart, fullcart, finnprice;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> spinnerdatalist;
    ValueEventListener listener;
    Chip chip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        resimagepd = findViewById(R.id.resimagepd);
        resaddfinal = findViewById(R.id.resfulladdress);
        cartbutt = findViewById(R.id.firstfloat);
        chip = findViewById(R.id.chips);
        bc = new DBHelper(Restaurant_Details_Activity.this).getcouncart();
//        chip.setChecked(true);
//        chip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();
        reskey = getIntent().getStringExtra("ProductKey");

        //firebase instances
        catref = FirebaseDatabase.getInstance().getReference().child("FoodCategories");
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        menuref = FirebaseDatabase.getInstance().getReference().child("MenuItems").child(reskey);
        databaseReferencerate = FirebaseDatabase.getInstance().getReference().child("Users");
        ratedb = FirebaseDatabase.getInstance().getReference().child("FoodRatings");


//        spinnerdatalist = new ArrayList<>();
//        arrayAdapter = new ArrayAdapter<String>(Restaurant_Details_Activity.this,
//                R.layout.support_simple_spinner_dropdown_item,
//                spinnerdatalist);
//
//        spincat.setAdapter(arrayAdapter);
//        retreivedata();


        cartbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartPage.class);
                startActivity(intent);
            }
        });

        menufetchrecyclerview = findViewById(R.id.menurecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        menufetchrecyclerview.setLayoutManager(linearLayoutManager);
        menufetchrecyclerview.setHasFixedSize(true);


        reference.child(reskey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resname = snapshot.child("restaurantname").getValue().toString();
                String resownername = snapshot.child("resowner").getValue().toString();
                String resownerphone = snapshot.child("resownerphone").getValue().toString();
                String restaurantaddress = snapshot.child("restaurantaddress").getValue().toString();
                String resowner = snapshot.child("resowner").getValue().toString();
                resimagefinal = snapshot.child("resimage").getValue().toString();

                toolBarLayout.setTitle(resname);
                resaddfinal.setText(restaurantaddress);
                Glide.with(Restaurant_Details_Activity.this).load(resimagefinal).into(resimagepd);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fetchmenuitems();


        filterrecycler = findViewById(R.id.chiprecycler);
        linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        filterrecycler.setLayoutManager(linearLayoutManager2);
        filterrecycler.setHasFixedSize(true);
        filteritems();


    }

    private void filteritems() {

        optionsfilter = new FirebaseRecyclerOptions.Builder<ModelCategories>().setQuery(catref, ModelCategories.class).build();
        adapterfilter = new FirebaseRecyclerAdapter<ModelCategories, CatHolder>(optionsfilter) {
            @Override
            protected void onBindViewHolder(@NonNull final CatHolder holder, int position, @NonNull ModelCategories model) {
                holder.chip.setText(model.getCategoryName());
                //getRef(holder.getAdapterPosition()).getKey();
                holder.chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            keyfinal = holder.chip.getText().toString();
                            filtercategory("");
                        }
                    }
                });

                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        fetchmenuitems();
                        holder.chip.setChecked(false);
                    }
                });
            }

            @NonNull
            @Override
            public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlechipitem, parent, false);
                CatHolder viewholder = new CatHolder(view);
                return viewholder;
            }
        };
        adapterfilter.startListening();
        filterrecycler.setAdapter(adapterfilter);
    }

    private void retreivedata() {

        listener = catref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.child("CategoryName").getValue(String.class);
                    spinnerdatalist.add(key);

                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void fetchmenuitems() {
        //Query query = menuref.orderByChild("resid").equalTo(reskey);
        options = new FirebaseRecyclerOptions.Builder<MenuModel>().setQuery(menuref, MenuModel.class).build();
        adapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewholder>(options) {
            @NonNull
            @Override
            public MenuViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemformenu, parent, false);
                MenuViewholder viewholder = new MenuViewholder(view);
                return viewholder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final MenuViewholder holder, int position, @NonNull final MenuModel model) {

//                String menufinalname = model.getItemname().substring(0, Math.min(10, model.getItemname().length() - 1)) + "..";
                String menufinalname = model.getItemname();
                Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.menuimage);
                holder.menuname.setText(menufinalname);
                String stat = model.getStatus();
                holder.vanishedlayout.setVisibility(View.GONE);
                if (stat.equals("off")) {
                    holder.vanishedlayout.setVisibility(View.VISIBLE);
                    holder.vanishedlayout.bringToFront();
                    holder.l1.setAlpha((float) 0.2);
                    holder.addtocart.setEnabled(false);
                }
                final String originalprice = model.getOriginalprice();

                if (originalprice.equals("")) {
                    holder.menu_original_price.setVisibility(View.GONE);
                    holder.menu_final_price.setText(model.getSmallsizeprice());
                    holder.ruppe.setVisibility(View.GONE);
                } else {
                    holder.menu_final_price.setText(model.getFinalprice());
                    holder.menu_original_price.setText(model.getOriginalprice());
                    holder.menu_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.from.setVisibility(View.GONE);
                    holder.menu_original_price.setTextSize(20);
                    holder.menu_final_price.setTextSize(20);
                }
                if (bc == 0) {
                    cartbutt.setCount(0);
                } else if (bc > 0) {
                    cartbutt.setCount(bc);

                }
                holder.addtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(Restaurant_Details_Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.newdialogforaddingcart);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow().setAttributes(lp);
                        final TextView menuname = dialog.findViewById(R.id.namemenu);
                        final ImageView menuimage = dialog.findViewById(R.id.menuimage);
                        final LinearLayout linearLayout = dialog.findViewById(R.id.sizelayout);
                        final LinearLayout pricevanish = dialog.findViewById(R.id.pricevanished);
                        final TextView orprice = dialog.findViewById(R.id.orpriceitemdialog);
                        final TextView finprice = dialog.findViewById(R.id.finpriceitemdialog);
                        final TextView total = dialog.findViewById(R.id.textView4);
                        final TextView fullpricedialog = dialog.findViewById(R.id.fullpricedialog);
                        final CheckBox half = dialog.findViewById(R.id.half);
                        final CheckBox full = dialog.findViewById(R.id.full);
                        final ElegantNumberButton elegantNumberButton = dialog.findViewById(R.id.elegant_button);
                        final TextView halfpricedialog = dialog.findViewById(R.id.halfpricedialog);
                        final Button button = dialog.findViewById(R.id.button);
                        final RatingBar ratingBar = dialog.findViewById(R.id.ratinffood);
                        final EditText ratingtext = dialog.findViewById(R.id.ratinginfo);
                        final Button submitrating = dialog.findViewById(R.id.submitrating);
                        final RatingBar finalrate = dialog.findViewById(R.id.finalrating);
                        final TextView finalratetext = dialog.findViewById(R.id.textfinalrate);


                        submitrating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String ratecomment = ratingtext.getText().toString();
                                final String ratevalue = String.valueOf(ratingBar.getRating());
                                databaseReferencerate.child(user).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String uphone = snapshot.child("customerMobile").getValue(String.class);
                                        RatingModel rating = new RatingModel(uphone, model.getMenuid(), ratevalue, ratecomment, model.getItemname());
                                        ratedb.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(dialog.getContext(), "Thanks for your Feedback!", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        Query foodratingfetch = ratedb.orderByChild("foodid").equalTo(model.getMenuid());

                        foodratingfetch.addValueEventListener(new ValueEventListener() {

                            int countrate = 0;
                            double sum = 0.0;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postsnap : snapshot.getChildren()) {
                                    RatingModel ratingModel = postsnap.getValue(RatingModel.class);
                                    sum += Double.parseDouble(ratingModel.getRateValue());
                                    countrate++;
                                }
                                if (countrate != 0) {
                                    double avg = sum / countrate;
                                    finalrate.setRating((float) avg);
                                    String ratetext = String.valueOf(avg);

                                    finalratetext.setText(ratetext + " Out of 5 Stars");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        menuname.setText(model.getItemname());
                        Glide.with(dialog.getContext()).load(model.getProductimage()).into(menuimage);
                        menuimagecart = model.getProductimage();
                        if (!originalprice.equals("")) {
                            linearLayout.setVisibility(View.GONE);
                            orprice.setText(model.getOriginalprice());
                            finprice.setText(model.getFinalprice());
                            orprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                @Override
                                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                    count = (newValue * (Integer.parseInt(finprice.getText().toString())));
                                    total.setText(String.valueOf(count));
                                    menuquantityforcart = elegantNumberButton.getNumber();
                                    menupriceforcart = total.getText().toString();
                                    finnprice = finprice.getText().toString();
                                }
                            });
                            menunameforcart = menuname.getText().toString();
                            menuidforcart = model.getMenuid();
                            halfcart = "";
                            fullcart = "";
                            menufullpriceforcart = "";
                            menuhalfpriceforcart = "";
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    onlyiforandfin();
                                    newdbcart();
                                    dialog.dismiss();
                                }
                            });

                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            pricevanish.setVisibility(View.GONE);
                            final String halfprice = model.getSmallsizeprice();
                            String fullprice = model.getLargesizeprice();
                            fullpricedialog.setText(fullprice);
                            halfpricedialog.setText(halfprice);
                            half.setText(model.getItemsmallsizename());
                            full.setText(model.getItemlargesizename());
                            elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                @Override
                                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                    if (newValue > 0) {
                                        Toast.makeText(getApplicationContext(), "Please Choose Size", Toast.LENGTH_SHORT).show();
                                        elegantNumberButton.setNumber("0");
                                    }
                                }
                            });
                            half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        full.setChecked(false);
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                int b = Integer.parseInt(halfpricedialog.getText().toString());
                                                int finalcount = newValue * b;
                                                total.setText(String.valueOf(finalcount));
                                                menuquantityforcart = elegantNumberButton.getNumber();
                                                menuhalfpriceforcart = halfpricedialog.getText().toString();

                                            }
                                        });
                                        menunameforcart = menuname.getText().toString();
                                        menuidforcart = model.getMenuid();
                                        halfcart = half.getText().toString();
                                        fullcart = "";
                                        menufullpriceforcart = "";
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                               onlyiforandfin();
                                                newdbcart();
                                                dialog.dismiss();
                                            }
                                        });


                                    } else {
                                        total.setText("0");
                                        elegantNumberButton.setNumber("0");
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                elegantNumberButton.setNumber("0");
                                            }
                                        });
                                    }
                                }
                            });
                            full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        half.setChecked(false);
                                        total.setText("0");
                                        elegantNumberButton.setNumber("0");
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                int b = Integer.parseInt(fullpricedialog.getText().toString());
                                                int finalcount = newValue * b;
                                                total.setText(String.valueOf(finalcount));
                                                menuquantityforcart = elegantNumberButton.getNumber();
                                                menufullpriceforcart = fullpricedialog.getText().toString();

                                            }
                                        });
                                        menunameforcart = menuname.getText().toString();
                                        menuidforcart = model.getMenuid();
                                        fullcart = full.getText().toString();
                                        halfcart = "";
                                        menuhalfpriceforcart = "";
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                newdbcart();
                                                dialog.dismiss();
                                            }
                                        });
                                    } else {
                                        total.setText("0");
                                        elegantNumberButton.setNumber("0");
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                elegantNumberButton.setNumber("0");
                                            }
                                        });

                                    }
                                }
                            });
                        }

                        dialog.show();

                    }
                });

            }
        };
        adapter.startListening();
        menufetchrecyclerview.setAdapter(adapter);


    }

    public void newdbcart() {
        new DBHelper(getBaseContext()).addToCart(new Order(menuidforcart, menunameforcart, menuquantityforcart, finnprice, menuhalfpriceforcart, menufullpriceforcart, halfcart, fullcart));
        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_LONG).show();

    }

    public void filtercategory(String data) {
        data = keyfinal;
        final Query query = menuref.orderByChild("itemcat").equalTo(data);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    options = new FirebaseRecyclerOptions.Builder<MenuModel>().setQuery(query, MenuModel.class).build();
                    adapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewholder>(options) {
                        @NonNull
                        @Override
                        public MenuViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemformenu, parent, false);
                            MenuViewholder viewholder = new MenuViewholder(view);
                            return viewholder;
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull final MenuViewholder holder, int position, @NonNull final MenuModel model) {
                            String menufinalname = model.getItemname();
                            Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.menuimage);
                            holder.menuname.setText(menufinalname);
                            String stat = model.getStatus();
                            holder.vanishedlayout.setVisibility(View.GONE);
                            if (stat.equals("off")) {
                                holder.vanishedlayout.setVisibility(View.VISIBLE);
                                holder.vanishedlayout.bringToFront();
                                holder.l1.setAlpha((float) 0.2);
                                holder.addtocart.setEnabled(false);
                            }
                            final String originalprice = model.getOriginalprice();

                            if (originalprice.equals("")) {
                                holder.menu_original_price.setVisibility(View.GONE);
                                holder.menu_final_price.setText(model.getSmallsizeprice());
                                holder.ruppe.setVisibility(View.GONE);
                            } else {
                                holder.menu_final_price.setText(model.getFinalprice());
                                holder.menu_original_price.setText(model.getOriginalprice());
                                holder.menu_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.from.setVisibility(View.GONE);
                                holder.menu_original_price.setTextSize(20);
                                holder.menu_final_price.setTextSize(20);
                            }
                            if (bc == 0) {
                                cartbutt.setCount(0);
                            } else if (bc > 0) {
                                cartbutt.setCount(bc);

                            }
                            holder.addtocart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(Restaurant_Details_Activity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.newdialogforaddingcart);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    lp.gravity = Gravity.CENTER;
                                    dialog.getWindow().setAttributes(lp);
                                    final TextView menuname = dialog.findViewById(R.id.namemenu);
                                    final ImageView menuimage = dialog.findViewById(R.id.menuimage);
                                    final LinearLayout linearLayout = dialog.findViewById(R.id.sizelayout);
                                    final LinearLayout pricevanish = dialog.findViewById(R.id.pricevanished);
                                    final TextView orprice = dialog.findViewById(R.id.orpriceitemdialog);
                                    final TextView finprice = dialog.findViewById(R.id.finpriceitemdialog);
                                    final TextView total = dialog.findViewById(R.id.textView4);
                                    final TextView fullpricedialog = dialog.findViewById(R.id.fullpricedialog);
                                    final CheckBox half = dialog.findViewById(R.id.half);
                                    final CheckBox full = dialog.findViewById(R.id.full);
                                    final ElegantNumberButton elegantNumberButton = dialog.findViewById(R.id.elegant_button);
                                    final TextView halfpricedialog = dialog.findViewById(R.id.halfpricedialog);
                                    final Button button = dialog.findViewById(R.id.button);
                                    final RatingBar ratingBar = dialog.findViewById(R.id.ratinffood);
                                    final EditText ratingtext = dialog.findViewById(R.id.ratinginfo);
                                    final Button submitrating = dialog.findViewById(R.id.submitrating);
                                    final RatingBar finalrate = dialog.findViewById(R.id.finalrating);
                                    final TextView finalratetext = dialog.findViewById(R.id.textfinalrate);


                                    submitrating.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String ratecomment = ratingtext.getText().toString();
                                            final String ratevalue = String.valueOf(ratingBar.getRating());
                                            databaseReferencerate.child(user).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String uphone = snapshot.child("customerMobile").getValue(String.class);
                                                    RatingModel rating = new RatingModel(uphone, model.getMenuid(), ratevalue, ratecomment, model.getItemname());
                                                    ratedb.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(dialog.getContext(), "Thanks for your Feedback!", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    });

                                    Query foodratingfetch = ratedb.orderByChild("foodid").equalTo(model.getMenuid());

                                    foodratingfetch.addValueEventListener(new ValueEventListener() {

                                        int countrate = 0;
                                        double sum = 0.0;

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot postsnap : snapshot.getChildren()) {
                                                RatingModel ratingModel = postsnap.getValue(RatingModel.class);
                                                sum += Double.parseDouble(ratingModel.getRateValue());
                                                countrate++;
                                            }
                                            if (countrate != 0) {
                                                double avg = sum / countrate;
                                                finalrate.setRating((float) avg);
                                                String ratetext = String.valueOf(avg);

                                                finalratetext.setText(ratetext + " Out of 5 Stars");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    menuname.setText(model.getItemname());
                                    Glide.with(dialog.getContext()).load(model.getProductimage()).into(menuimage);
                                    menuimagecart = model.getProductimage();
                                    if (!originalprice.equals("")) {
                                        linearLayout.setVisibility(View.GONE);
                                        orprice.setText(model.getOriginalprice());
                                        finprice.setText(model.getFinalprice());
                                        orprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                count = (newValue * (Integer.parseInt(finprice.getText().toString())));
                                                total.setText(String.valueOf(count));
                                                menuquantityforcart = elegantNumberButton.getNumber();
                                                menupriceforcart = total.getText().toString();
                                                finnprice = finprice.getText().toString();
                                            }
                                        });
                                        menunameforcart = menuname.getText().toString();
                                        menuidforcart = model.getMenuid();
                                        halfcart = "";
                                        fullcart = "";
                                        menufullpriceforcart = "";
                                        menuhalfpriceforcart = "";
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                    onlyiforandfin();
                                                newdbcart();
                                                dialog.dismiss();
                                            }
                                        });

                                    } else {
                                        linearLayout.setVisibility(View.VISIBLE);
                                        pricevanish.setVisibility(View.GONE);
                                        final String halfprice = model.getSmallsizeprice();
                                        String fullprice = model.getLargesizeprice();
                                        fullpricedialog.setText(fullprice);
                                        halfpricedialog.setText(halfprice);
                                        half.setText(model.getItemsmallsizename());
                                        full.setText(model.getItemlargesizename());
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                if (newValue > 0) {
                                                    Toast.makeText(getApplicationContext(), "Please Choose Size", Toast.LENGTH_SHORT).show();
                                                    elegantNumberButton.setNumber("0");
                                                }
                                            }
                                        });
                                        half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    full.setChecked(false);
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            int b = Integer.parseInt(halfpricedialog.getText().toString());
                                                            int finalcount = newValue * b;
                                                            total.setText(String.valueOf(finalcount));
                                                            menuquantityforcart = elegantNumberButton.getNumber();
                                                            menuhalfpriceforcart = halfpricedialog.getText().toString();

                                                        }
                                                    });
                                                    menunameforcart = menuname.getText().toString();
                                                    menuidforcart = model.getMenuid();
                                                    halfcart = half.getText().toString();
                                                    fullcart = "";
                                                    menufullpriceforcart = "";
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
//                                               onlyiforandfin();
                                                            newdbcart();
                                                            dialog.dismiss();
                                                        }
                                                    });


                                                } else {
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                            elegantNumberButton.setNumber("0");
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    half.setChecked(false);
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            int b = Integer.parseInt(fullpricedialog.getText().toString());
                                                            int finalcount = newValue * b;
                                                            total.setText(String.valueOf(finalcount));
                                                            menuquantityforcart = elegantNumberButton.getNumber();
                                                            menufullpriceforcart = fullpricedialog.getText().toString();

                                                        }
                                                    });
                                                    menunameforcart = menuname.getText().toString();
                                                    menuidforcart = model.getMenuid();
                                                    fullcart = full.getText().toString();
                                                    halfcart = "";
                                                    menuhalfpriceforcart = "";
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            newdbcart();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                } else {
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                            elegantNumberButton.setNumber("0");
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }

                                    dialog.show();

                                }
                            });

                        }
                    };
                    adapter.startListening();
                    menufetchrecyclerview.setAdapter(adapter);

                }
                else {
                    options = new FirebaseRecyclerOptions.Builder<MenuModel>().setQuery(query, MenuModel.class).build();
                    adapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewholder>(options) {
                        @NonNull
                        @Override
                        public MenuViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemformenu, parent, false);
                            MenuViewholder viewholder = new MenuViewholder(view);
                            return viewholder;
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull final MenuViewholder holder, int position, @NonNull final MenuModel model) {
                            String menufinalname = model.getItemname();
                            Glide.with(getApplicationContext()).load(model.getProductimage()).into(holder.menuimage);
                            holder.menuname.setText(menufinalname);
                            String stat = model.getStatus();
                            holder.vanishedlayout.setVisibility(View.GONE);
                            if (stat.equals("off")) {
                                holder.vanishedlayout.setVisibility(View.VISIBLE);
                                holder.vanishedlayout.bringToFront();
                                holder.l1.setAlpha((float) 0.2);
                                holder.addtocart.setEnabled(false);
                            }
                            final String originalprice = model.getOriginalprice();

                            if (originalprice.equals("")) {
                                holder.menu_original_price.setVisibility(View.GONE);
                                holder.menu_final_price.setText(model.getSmallsizeprice());
                                holder.ruppe.setVisibility(View.GONE);
                            } else {
                                holder.menu_final_price.setText(model.getFinalprice());
                                holder.menu_original_price.setText(model.getOriginalprice());
                                holder.menu_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.from.setVisibility(View.GONE);
                                holder.menu_original_price.setTextSize(20);
                                holder.menu_final_price.setTextSize(20);
                            }
                            if (bc == 0) {
                                cartbutt.setCount(0);
                            } else if (bc > 0) {
                                cartbutt.setCount(bc);

                            }
                            holder.addtocart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(Restaurant_Details_Activity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.newdialogforaddingcart);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    lp.gravity = Gravity.CENTER;
                                    dialog.getWindow().setAttributes(lp);
                                    final TextView menuname = dialog.findViewById(R.id.namemenu);
                                    final ImageView menuimage = dialog.findViewById(R.id.menuimage);
                                    final LinearLayout linearLayout = dialog.findViewById(R.id.sizelayout);
                                    final LinearLayout pricevanish = dialog.findViewById(R.id.pricevanished);
                                    final TextView orprice = dialog.findViewById(R.id.orpriceitemdialog);
                                    final TextView finprice = dialog.findViewById(R.id.finpriceitemdialog);
                                    final TextView total = dialog.findViewById(R.id.textView4);
                                    final TextView fullpricedialog = dialog.findViewById(R.id.fullpricedialog);
                                    final CheckBox half = dialog.findViewById(R.id.half);
                                    final CheckBox full = dialog.findViewById(R.id.full);
                                    final ElegantNumberButton elegantNumberButton = dialog.findViewById(R.id.elegant_button);
                                    final TextView halfpricedialog = dialog.findViewById(R.id.halfpricedialog);
                                    final Button button = dialog.findViewById(R.id.button);
                                    final RatingBar ratingBar = dialog.findViewById(R.id.ratinffood);
                                    final EditText ratingtext = dialog.findViewById(R.id.ratinginfo);
                                    final Button submitrating = dialog.findViewById(R.id.submitrating);
                                    final RatingBar finalrate = dialog.findViewById(R.id.finalrating);
                                    final TextView finalratetext = dialog.findViewById(R.id.textfinalrate);


                                    submitrating.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String ratecomment = ratingtext.getText().toString();
                                            final String ratevalue = String.valueOf(ratingBar.getRating());
                                            databaseReferencerate.child(user).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String uphone = snapshot.child("customerMobile").getValue(String.class);
                                                    RatingModel rating = new RatingModel(uphone, model.getMenuid(), ratevalue, ratecomment, model.getItemname());
                                                    ratedb.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(dialog.getContext(), "Thanks for your Feedback!", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    });

                                    Query foodratingfetch = ratedb.orderByChild("foodid").equalTo(model.getMenuid());

                                    foodratingfetch.addValueEventListener(new ValueEventListener() {

                                        int countrate = 0;
                                        double sum = 0.0;

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot postsnap : snapshot.getChildren()) {
                                                RatingModel ratingModel = postsnap.getValue(RatingModel.class);
                                                sum += Double.parseDouble(ratingModel.getRateValue());
                                                countrate++;
                                            }
                                            if (countrate != 0) {
                                                double avg = sum / countrate;
                                                finalrate.setRating((float) avg);
                                                String ratetext = String.valueOf(avg);

                                                finalratetext.setText(ratetext + " Out of 5 Stars");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    menuname.setText(model.getItemname());
                                    Glide.with(dialog.getContext()).load(model.getProductimage()).into(menuimage);
                                    menuimagecart = model.getProductimage();
                                    if (!originalprice.equals("")) {
                                        linearLayout.setVisibility(View.GONE);
                                        orprice.setText(model.getOriginalprice());
                                        finprice.setText(model.getFinalprice());
                                        orprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                count = (newValue * (Integer.parseInt(finprice.getText().toString())));
                                                total.setText(String.valueOf(count));
                                                menuquantityforcart = elegantNumberButton.getNumber();
                                                menupriceforcart = total.getText().toString();
                                                finnprice = finprice.getText().toString();
                                            }
                                        });
                                        menunameforcart = menuname.getText().toString();
                                        menuidforcart = model.getMenuid();
                                        halfcart = "";
                                        fullcart = "";
                                        menufullpriceforcart = "";
                                        menuhalfpriceforcart = "";
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                    onlyiforandfin();
                                                newdbcart();
                                                dialog.dismiss();
                                            }
                                        });

                                    } else {
                                        linearLayout.setVisibility(View.VISIBLE);
                                        pricevanish.setVisibility(View.GONE);
                                        final String halfprice = model.getSmallsizeprice();
                                        String fullprice = model.getLargesizeprice();
                                        fullpricedialog.setText(fullprice);
                                        halfpricedialog.setText(halfprice);
                                        half.setText(model.getItemsmallsizename());
                                        full.setText(model.getItemlargesizename());
                                        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                if (newValue > 0) {
                                                    Toast.makeText(getApplicationContext(), "Please Choose Size", Toast.LENGTH_SHORT).show();
                                                    elegantNumberButton.setNumber("0");
                                                }
                                            }
                                        });
                                        half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    full.setChecked(false);
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            int b = Integer.parseInt(halfpricedialog.getText().toString());
                                                            int finalcount = newValue * b;
                                                            total.setText(String.valueOf(finalcount));
                                                            menuquantityforcart = elegantNumberButton.getNumber();
                                                            menuhalfpriceforcart = halfpricedialog.getText().toString();

                                                        }
                                                    });
                                                    menunameforcart = menuname.getText().toString();
                                                    menuidforcart = model.getMenuid();
                                                    halfcart = half.getText().toString();
                                                    fullcart = "";
                                                    menufullpriceforcart = "";
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
//                                               onlyiforandfin();
                                                            newdbcart();
                                                            dialog.dismiss();
                                                        }
                                                    });


                                                } else {
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                            elegantNumberButton.setNumber("0");
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    half.setChecked(false);
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            int b = Integer.parseInt(fullpricedialog.getText().toString());
                                                            int finalcount = newValue * b;
                                                            total.setText(String.valueOf(finalcount));
                                                            menuquantityforcart = elegantNumberButton.getNumber();
                                                            menufullpriceforcart = fullpricedialog.getText().toString();

                                                        }
                                                    });
                                                    menunameforcart = menuname.getText().toString();
                                                    menuidforcart = model.getMenuid();
                                                    fullcart = full.getText().toString();
                                                    halfcart = "";
                                                    menuhalfpriceforcart = "";
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            newdbcart();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                } else {
                                                    total.setText("0");
                                                    elegantNumberButton.setNumber("0");
                                                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                        @Override
                                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                                                            Toast.makeText(getApplicationContext(), "Please Add Size..", Toast.LENGTH_SHORT).show();
                                                            elegantNumberButton.setNumber("0");
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }

                                    dialog.show();

                                }
                            });

                        }
                    };
                    adapter.startListening();
                    menufetchrecyclerview.setAdapter(adapter);
                    Toast.makeText(getApplicationContext(), "No Item Found on "+keyfinal, Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}