package com.example.andrea.memorableplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{


   private static LinkedList<String> addresses = new LinkedList<>();
   private static ArrayAdapter<String> stringArrayAdapter;
   public static ArrayList<Place> myPlaces = new ArrayList<>();
   private DBHandler dbHandler;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(this);

      dbHandler = new DBHandler(this, null);

      ListView listView = (ListView) findViewById(R.id.listView);
      myPlaces = dbHandler.getPlaces();
      getAddresses();
      stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addresses);
      listView.setAdapter(stringArrayAdapter);

      listView.setOnItemClickListener(this);
      listView.setOnItemLongClickListener(this);



   }

   @Override
   public void onClick(View view)
   {
      Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
      intent.putExtra("addNew", true);
      startActivity(intent);
   }

   @Override
   public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
   {
      Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
      intent.putExtra("locationInfo", position);
      intent.putExtra("addNew", false);
      startActivity(intent);
   }

   private static void getAddresses()
   {
      addresses.clear();
      for (Place p : myPlaces)
      {
         addresses.add(p.get_address());
      }

   }


   public static void onAddressesChanged()
   {
      getAddresses();
      stringArrayAdapter.notifyDataSetChanged();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      int id = item.getItemId();

      if (id == R.id.save)
      {
         Log.i("Save", "selected");
         for (Place p: myPlaces)
         {
            if (dbHandler.isPlaceInDB(p))
            {
               Log.i("PLACE -- Address", p.get_address());
               Log.i("PLACE -- Lat", String.valueOf(p.get_lat()));
               Log.i("PLACE -- Lng", String.valueOf(p.get_lng()));
               dbHandler.addPlace(p);
            }

         }
         Log.i("Database", dbHandler.toString());
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
   {
      Place placeToRemove = myPlaces.remove(position);
      dbHandler.deletePlace(placeToRemove.get_address());
      onAddressesChanged();

      return true;
   }
}
