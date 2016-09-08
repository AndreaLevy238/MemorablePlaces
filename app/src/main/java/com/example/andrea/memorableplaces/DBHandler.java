package com.example.andrea.memorableplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper
{
   private static final int DB_VERSION = 2;
   public static final String DB_NAME = "places.db";
   public static final String TABLE_NAME = "places";
   public static final String COLUMN_ID = "_id";
   public static final String COLUMN_ADDRESS = "address";
   public static final String COLUMN_LAT = "lat";
   public static final String COLUMN_LNG = "lng";

   public DBHandler(Context context, SQLiteDatabase.CursorFactory factory)
   {
      super(context, DB_NAME, factory, DB_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db)
   {
      String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
              COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
              COLUMN_ADDRESS + " TEXT, " +
              COLUMN_LAT + " DOUBLE, " +
              COLUMN_LNG + " DOUBLE" +
              ");";
      db.execSQL(sql);

   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
   {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
   }

   //Adds a new row to the database
   public void addPlace(Place place)
   {
      ContentValues values = new ContentValues();
      values.put(COLUMN_ADDRESS, place.get_address());
      values.put(COLUMN_LAT, place.get_lat());
      values.put(COLUMN_LNG, place.get_lng());
      SQLiteDatabase db = getWritableDatabase();
      db.insert(TABLE_NAME, null, values);
      db.close();
   }

   //Deletes a product from the Database
   public void deletePlace(String address)
   {
      SQLiteDatabase db = getWritableDatabase();
      db.execSQL("DELETE FROM " + TABLE_NAME +  " WHERE " + COLUMN_ADDRESS + "=\"" + address + "\";");
      db.close();
   }

   @Override
   public String toString()
   {
      String s = "";
      SQLiteDatabase db = getReadableDatabase();
      String sql = "SELECT * FROM " + TABLE_NAME;
      Cursor c = db.rawQuery(sql,null);
      boolean hasNext = c.moveToFirst();
      while(hasNext)
      {
         if (c.getString(c.getColumnIndex(COLUMN_ADDRESS)) != null)
         {
            s += c.getString(c.getColumnIndex(COLUMN_ADDRESS));
            s += "\n";
         }
         hasNext = c.moveToNext();
      }
      c.close();
      db.close();
      return s;
   }

   public boolean isPlaceInDB(Place place)
   {
      SQLiteDatabase db = getReadableDatabase();
      String lat = String.valueOf(place.get_lat());
      String lng = String.valueOf(place.get_lng());
      String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + lat + " = " + COLUMN_LAT
              + " AND " + lng + " = " + COLUMN_LNG;
      Cursor c = db.rawQuery(sql, null);
      int numFound = c.getCount();
      c.close();
      return !(numFound > 0);

   }

   public ArrayList<Place> getPlaces()
   {
      ArrayList<Place> mySavedPlaces = new ArrayList<>();
      SQLiteDatabase db = getReadableDatabase();
      String sql = "SELECT * FROM " + TABLE_NAME;
      Cursor c = db.rawQuery(sql,null);
      boolean hasNext = c.moveToFirst();
      while(hasNext)
      {
         Place place = new Place();
         place.set_address(c.getString(c.getColumnIndex(COLUMN_ADDRESS)));
         place.set_lat(c.getDouble(c.getColumnIndex(COLUMN_LAT)));
         place.set_lng(c.getDouble(c.getColumnIndex(COLUMN_LNG)));
         mySavedPlaces.add(place);
         hasNext = c.moveToNext();
      }
      c.close();
      return mySavedPlaces;
   }
}
