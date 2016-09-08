package com.example.andrea.memorableplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

   private static ArrayList<String> addresses = new ArrayList<>();
   private static ArrayAdapter<String> stringArrayAdapter;
   public static ArrayList<Place> myPlaces = new ArrayList<>();


   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View view)
         {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("addNew", true);
            startActivity(intent);
         }
      });

      ListView listView = (ListView) findViewById(R.id.listView);
      getAddresses();
      stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addresses);
      listView.setAdapter(stringArrayAdapter);

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
         {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("locationInfo", position);
            intent.putExtra("addNew", false);
            startActivity(intent);

         }
      });

   }

   private static void getAddresses()
   {
      addresses.clear();
      for (int i = 0; i < myPlaces.size(); i++)
      {
         addresses.add(myPlaces.get(i).get_address());
      }

   }

   public static void onAddressesChanged()
   {
      getAddresses();
      stringArrayAdapter.notifyDataSetChanged();
   }

}
