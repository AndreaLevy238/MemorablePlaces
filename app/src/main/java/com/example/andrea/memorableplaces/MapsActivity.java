package com.example.andrea.memorableplaces;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener
{

   private GoogleMap mMap;
   private LocationManager locationManager;
   private String provider;
   private int MY_PERMISSIONS_REQUEST_LOCATION = 99;
   private Location lastLocation;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_maps);

      // Obtain the SupportMapFragment and get notified when the map is ready to be used.
      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map);
      ActionBar actionBar = getActionBar();

      if (actionBar != null)
      {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }

      mapFragment.getMapAsync(this);

   }

   /**
    *
    * @param item gets the menu item selected
    * @return whether the action specified is completed
    */
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case android.R.id.home:
            this.finish();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }

   }
   /**
    * Gets the location of the user
    */
   public void getLocation()
   {
      if (provider != null)
      {
         try
         {
            locationManager.requestLocationUpdates(provider, 0, 0, this);
            lastLocation = locationManager.getLastKnownLocation(provider);
            if (lastLocation != null)
            {
               Log.i("Location Info", "Location achieved!");
               onLocationChanged(lastLocation);
            }
            else
            {
               Log.i("Location Info", "No location :(");
            }
         }
         catch (SecurityException e)
         {
            checkLocationPermission();
         }
      }
      else
      {
         boolean checkLocation = checkLocationPermission();
         if (checkLocation)
         {
            getLocation();
         }
      }
   }

   @Override
   public void onMapReady(GoogleMap googleMap)
   {
      mMap = googleMap;

      Intent intent = getIntent();
      boolean addNew = intent.getBooleanExtra("addNew", false);

      if (addNew)
      {
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         provider = locationManager.getBestProvider(new Criteria(), false);
         getLocation();
         mMap.setOnMapLongClickListener(this);
      }
      else
      {
         int locationInfo = intent.getIntExtra("locationInfo", -1);
         Log.i("location info", String.valueOf(locationInfo));
         Place place = MainActivity.myPlaces.get(locationInfo);
         LatLng latLng = place.get_LatLng();
         mMap.addMarker(new MarkerOptions().position(latLng).title(place.get_address()));
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
      }

   }

   /**
    *
    * @param latLng the latitude and longitude given by the gps
    */
   @Override
   public void onMapLongClick(LatLng latLng)
   {

      String label;
      try
      {
         Geocoder g = new Geocoder(getApplicationContext());
         List<Address> addresses = g.getFromLocation(latLng.latitude, latLng.longitude, 1);
         if (addresses != null && addresses.size() > 0)
         {
            label = addresses.get(0).getAddressLine(0);
            double lat = latLng.latitude;
            double lng  = latLng.longitude;
            Place place = new Place(label, lat, lng);
            MainActivity.myPlaces.add(place);

         }
         else
         {
            label = new Date().toString();
         }
         mMap.addMarker(new MarkerOptions().position(latLng).title(label));
         MainActivity.onAddressesChanged();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }


   }

   /**
    *
    * @param location is the new location of the user
    */
   @Override
   public void onLocationChanged(Location location)
   {
      double lat = location.getLatitude();
      double lng = location.getLongitude();
      LatLng latLng = new LatLng(lat, lng);
      mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
   }

   @Override
   public void onStatusChanged(String s, int i, Bundle bundle)
   {

   }

   @Override
   public void onProviderEnabled(String s)
   {

   }

   @Override
   public void onProviderDisabled(String s)
   {

   }

   /**
    * This checks whether the user gives the app permission to use location
    * @return true if the location access has been granted, false otherwise
    */
   public boolean checkLocationPermission()
   {
      if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
      {
         ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
         return false;
      }
      return true;
   }
}
