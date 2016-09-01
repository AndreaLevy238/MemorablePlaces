package com.example.andrea.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> places;
    public static ArrayAdapter<String> stringArrayAdapter;
    public static ArrayList<LatLng> latLngs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        places = new ArrayList<>();
        places.add("Add a new place...");
        latLngs = new ArrayList<>();
        latLngs.add(new LatLng(0,0));
        ListView listView = (ListView) findViewById(R.id.listView);
        stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("locationInfo", position);
                if (position == 0) {
                    intent.putExtra("addNew", true);
                }
                else {
                    intent.putExtra("addNew", false);
                }
                startActivity(intent);
            }
        });

    }
}
