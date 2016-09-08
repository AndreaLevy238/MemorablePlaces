package com.example.andrea.memorableplaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Andrea on 09/07/2016.
 * A place is constructed using an address, which is a string
 * a latitude which is a double
 * and a longitude which is a double
 */
public class Place
{
   private int _id;
   private String _address;
   private Double _lat;
   private Double _lng;

   /**
    * @param address the first line of the address.
    * @param lat the latitude of the place
    * @param lng the longitude of the place
    */
   public Place(String address, Double lat, Double lng)
   {
      this._address = address;
      this._lat = lat;
      this._lng = lng;
   }

   /**
    * @param address the first line of the address
    * @param latLng the latidute and longitude of the place
    */
   public Place(String address, LatLng latLng)
   {
      this._address  = address;
      this._lat = latLng.latitude;
      this._lng = latLng.longitude;
   }
   public Place(){}


   public void set_id(int id)
   {
      this._id = id;
   }

   /**
    * @return the first line of the address
    */
   public String get_address()

   {
      return _address;
   }

   /**
    * @param address the first line of the address
    */

   public void set_address(String address)
   {
      this._address = address;
   }

   public Double get_lat()
   {
      return _lat;
   }


   public Double get_lng()
   {
      return _lng;
   }

   /**
    * Sets the latitude and longitude
    * @param latlng the latitude and longitude given by the gps
    */
   public void set_latlng(LatLng latlng)
   {
      this._lat = latlng.latitude;
      this._lng = latlng.longitude;
   }

   public LatLng get_LatLng()
   {
      return new LatLng(_lat, _lng);
   }
}
