package com.chattthedev.foodscentral.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.chattthedev.foodscentral.Database.DBHelper;
import com.chattthedev.foodscentral.Fragments.HomeFragment;
import com.chattthedev.foodscentral.Fragments.MyCartFragment;
import com.chattthedev.foodscentral.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Main_Dashboard extends AppCompatActivity {
    TextView deliverylocation;
    RelativeLayout relativeLayout;
    long back_pressed;
    BottomNavigationView bottomNav;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new HomeFragment();
                    switch (item.getItemId()) {
                        case R.id.cart:
                            selectedFragment = new MyCartFragment();
                            BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.cart);
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            badgeDrawable.setVisible(true);
                            Intent intent = new Intent(getApplicationContext(), CartPage.class);
                            startActivity(intent);
                            int bc = new DBHelper(Main_Dashboard.this).getcouncart();
                            badgeDrawable.setNumber(bc);
                            break;
                        case R.id.orders:
//                            selectedFragment = new SearchFragment();
//                            BadgeDrawable badgeDrawable1 = bottomNav.getOrCreateBadge(R.id.orders);
//                            badgeDrawable1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                            badgeDrawable1.setVisible(true);
                            break;
                        case R.id.profile:
//                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main__dashboard);
        Toolbar usertoolbar = findViewById(R.id.toolbaruser);
        setSupportActionBar(usertoolbar);
        getSupportActionBar().setTitle("Foods Central");
        usertoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        deliverylocation = findViewById(R.id.locationtrack);
        relativeLayout = findViewById(R.id.relative);
        bottomNav = findViewById(R.id.bottomnavigationview);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        //permissioncheck();
        bottomNav.bringToFront();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getcurrentlocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Main_Dashboard.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Main_Dashboard.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(Main_Dashboard.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                                String addressfor = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                // String a = addressfor + city + state + country + postalCode;
                                deliverylocation.setText(addressfor);
                                Snackbar.make(relativeLayout, "Delivery Location Set", BaseTransientBottomBar.LENGTH_LONG).show();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                }, Looper.getMainLooper());
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();





    }

    private void permissioncheck(){
        try {
            Dexter.withActivity(Main_Dashboard.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            getcurrentlocation();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", Main_Dashboard.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}