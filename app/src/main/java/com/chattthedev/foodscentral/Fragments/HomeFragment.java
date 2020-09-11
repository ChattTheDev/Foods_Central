package com.chattthedev.foodscentral.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.transition.Fade;

import com.bumptech.glide.Glide;
import com.chattthedev.foodscentral.Activities.Menu_From_Categories;
import com.chattthedev.foodscentral.Activities.Restaurant_Details_Activity;
import com.chattthedev.foodscentral.Models.ModelCategories;
import com.chattthedev.foodscentral.Models.ModelRestaurantDetails;
import com.chattthedev.foodscentral.R;
import com.chattthedev.foodscentral.Viewholders.HorizontalViewholder;
import com.chattthedev.foodscentral.Viewholders.RestaurantViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView, horrecyclerview;
    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager  horizontallmanager;
    FirebaseRecyclerAdapter<ModelRestaurantDetails, RestaurantViewholder> adapter;
    FirebaseRecyclerOptions<ModelRestaurantDetails> options;

    FirebaseRecyclerOptions<ModelCategories> catoptions;
    FirebaseRecyclerAdapter<ModelCategories, HorizontalViewholder> catadapter;
    View newview;
    DatabaseReference restaurantdb, catdb;
    ProgressBar progressBar;
    String key;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newview = inflater.inflate(R.layout.fragment_home, container, false);
        restaurantdb = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        catdb = FirebaseDatabase.getInstance().getReference().child("FoodCategories");

        recyclerView = newview.findViewById(R.id.viewpager);
        horrecyclerview = newview.findViewById(R.id.foodcat);
        progressBar = newview.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);
        linearLayoutManager = new LinearLayoutManager(newview.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);

        horizontallmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        horrecyclerview.setLayoutManager(horizontallmanager);
        horrecyclerview.setHasFixedSize(true);

        fetchrestaurantdetails();
        fetchcategories();


        return newview;
    }

    private void fetchrestaurantdetails() {

        options = new FirebaseRecyclerOptions.Builder<ModelRestaurantDetails>().setQuery(restaurantdb, ModelRestaurantDetails.class).build();
        adapter = new FirebaseRecyclerAdapter<ModelRestaurantDetails, RestaurantViewholder>(options) {
            @NonNull
            @Override
            public RestaurantViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemfetchrestaurants, parent, false);
                RestaurantViewholder restaurantViewholder = new RestaurantViewholder(view);
                return restaurantViewholder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final RestaurantViewholder holder, int position, @NonNull ModelRestaurantDetails model) {

                holder.resname.setText(model.getRestaurantname());
                holder.resaddress.setText(model.getRestaurantaddress());
                holder.servicetime.setText("From " + model.getResstarttime() + " To " + model.getResendtime());
                Glide.with(newview.getContext()).load(model.getResimage()).into(holder.resimage);
                progressBar.setVisibility(View.GONE);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(newview.getContext(), Restaurant_Details_Activity.class);
                        intent.putExtra("resname", holder.resname.getText().toString());
                        key = getRef(holder.getAdapterPosition()).getKey();
                        intent.putExtra("ProductKey", key);

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), holder.resimage, ViewCompat.getTransitionName(holder.resimage));
                        startActivity(intent, optionsCompat.toBundle());


                    }
                });


            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    private void fetchcategories() {
        catoptions = new FirebaseRecyclerOptions.Builder<ModelCategories>().setQuery(catdb, ModelCategories.class).build();
        catadapter = new FirebaseRecyclerAdapter<ModelCategories, HorizontalViewholder>(catoptions) {
            @Override
            protected void onBindViewHolder(@NonNull final HorizontalViewholder holder, int position, @NonNull ModelCategories model) {

                holder.catname.setText(model.getCategoryName());
                Glide.with(newview.getContext()).load(model.getImageUrl()).into(holder.catimage);
                //Picasso.get().load(model.getImageUrl()).into(holder.catimage);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Menu_From_Categories.class);
                        intent.putExtra("catname", holder.catname.getText().toString());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public HorizontalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleforfetchingcategories, parent, false);
                final HorizontalViewholder viewHolder = new HorizontalViewholder(v);
                return viewHolder;
            }
        };
        catadapter.startListening();
        horrecyclerview.setAdapter(catadapter);

    }
}
