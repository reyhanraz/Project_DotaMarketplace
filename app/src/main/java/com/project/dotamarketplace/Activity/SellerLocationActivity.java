package com.project.dotamarketplace.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.project.dotamarketplace.R;
import com.project.dotamarketplace.mapsFragment;

public class SellerLocationActivity extends AppCompatActivity {
    Fragment mapsFragment;
    Double latitude, longitude;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_location);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        name = intent.getStringExtra("name");
        init();

    }

    private void init() {
        mapsFragment = new mapsFragment(name, new LatLng(latitude, longitude));
        getSupportFragmentManager().beginTransaction().replace(R.id.mapsFrameLayout, mapsFragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}