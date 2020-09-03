package com.chingkai56.findhouse;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlinx.coroutines.GlobalScope;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.chingkai56.findhouse.PriceRangeAdapterKt.priceRangeData;

public class HouseMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //adapter to control list of price range item for 租金 tab
    private PriceRangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_maps);
//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RecyclerView verticalList = findViewById(R.id.recycler_vertical_list_conditions);
        //init price range adapter ,bind with recyclerview and add data in it
        adapter = new PriceRangeAdapter();
        verticalList.setAdapter(adapter);
        verticalList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.addData(priceRangeData());

        RemoteApiKt.test();

        ConstraintLayout container = findViewById(R.id.conditions_container);
        //set click listener to tab 租金
        TextView view = findViewById(R.id.textview_rent_price);

        view.setOnClickListener(v -> {
            if (container.getVisibility()==View.GONE){
                container.setVisibility(View.VISIBLE);
            }else {
                container.setVisibility(View.GONE);
            }
        });
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng taiwan = new LatLng(25.0473073,121.5413907);

        mMap.addMarker(new MarkerOptions().position(taiwan).title("Marker in Taiwan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taiwan, (float) 11.5));
    }
}
