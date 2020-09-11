package com.chattthedev.foodscentral.Activities;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chattthedev.foodscentral.Database.DBHelper;
import com.chattthedev.foodscentral.Models.MenuModel;
import com.chattthedev.foodscentral.Models.Order;
import com.chattthedev.foodscentral.Models.RatingModel;
import com.chattthedev.foodscentral.R;
import com.chattthedev.foodscentral.Viewholders.MenuViewholderonCat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu_From_Categories extends AppCompatActivity {
    RecyclerView itemrecycler;
    FirebaseRecyclerOptions<MenuModel> optionscategory;
    FirebaseRecyclerAdapter<MenuModel, MenuViewholderonCat> adaptercategory;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference catref, databaseReferencerate, ratedb;
    String key, user, resname, resimagefinal, menuimagecart, menunameforcart, menuquantityforcart, menupriceforcart, menuidforcart, menufullpriceforcart, menuhalfpriceforcart, halfcart, fullcart, finnprice;
    FirebaseUser firebaseUser;
    int count = 0;
    ImageView notfoundimage;
    TextView notfoundtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu__from__categories);
        key = getIntent().getStringExtra("catname");
        Toolbar toolbar = findViewById(R.id.toolbarcat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delicious " + key + " Items..");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        notfoundimage = findViewById(R.id.notfoundimage);
        notfoundtext = findViewById(R.id.notfoundtext);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = firebaseUser.getUid();

        catref = FirebaseDatabase.getInstance().getReference().child("MenuforCatFilter");
        databaseReferencerate = FirebaseDatabase.getInstance().getReference().child("Users");
        ratedb = FirebaseDatabase.getInstance().getReference().child("FoodRatings");

        itemrecycler = findViewById(R.id.recyclerviewcatpage);
        linearLayoutManager = new LinearLayoutManager(this);
        itemrecycler.setLayoutManager(linearLayoutManager);
        itemrecycler.setHasFixedSize(true);

        fetchcat();


    }

    private void fetchcat() {
        final Query query = catref.orderByChild("itemcat").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    optionscategory = new FirebaseRecyclerOptions.Builder<MenuModel>().setQuery(query, MenuModel.class).build();
                    adaptercategory = new FirebaseRecyclerAdapter<MenuModel, MenuViewholderonCat>(optionscategory) {
                        @NonNull
                        @Override
                        public MenuViewholderonCat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemforcatitems, parent, false);
                            MenuViewholderonCat viewholder = new MenuViewholderonCat(view);
                            return viewholder;
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull MenuViewholderonCat holder, int position, @NonNull final MenuModel model) {
                            holder.menuname.setText(model.getItemname());
                            holder.fromres.setText("From " + model.getResname());
                            Glide.with(Menu_From_Categories.this).load(model.getProductimage()).into(holder.menuimage);
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
                            holder.addtocart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(Menu_From_Categories.this);
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
                    adaptercategory.startListening();
                    itemrecycler.setAdapter(adaptercategory);
                }
                else {
                    notfoundimage.setVisibility(View.VISIBLE);
                    notfoundtext.setVisibility(View.VISIBLE);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void newdbcart() {
        new DBHelper(getBaseContext()).addToCart(new Order(menuidforcart, menunameforcart, menuquantityforcart, finnprice, menuhalfpriceforcart, menufullpriceforcart, halfcart, fullcart));
        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_LONG).show();

    }
}