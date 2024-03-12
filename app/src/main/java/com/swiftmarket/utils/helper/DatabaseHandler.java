package com.swiftmarket.utils.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.swiftmarket.data.LocalProductsModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private DatabaseHelperListener databaseHelperListener;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SwiftMarket.db";
    public static final String TABLE_NAME = "product";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String PRODUCT_QUANTITY = "productQuantity";
    public static final String PRODUCT_LOCATION = "productLocation";
    public static final String PRODUCT_IMAGE = "productImage";
    public static final String PRODUCT_CATEGORY = "productCategory";
    public static final String PRODUCT_DESCRIPTION = "productDescription";
    private SQLiteDatabase database;
    // Constructor
    public DatabaseHandler(Context context,DatabaseHelperListener databaseHelperListener) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.databaseHelperListener=databaseHelperListener;
    }

    // Create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table  in onCreate method
        db.execSQL("create table " + TABLE_NAME +
                " (" + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUCT_NAME + " VARCHAR, "
                + PRODUCT_PRICE + " VARCHAR,"
                + PRODUCT_QUANTITY + " VARCHAR,"
                + PRODUCT_LOCATION + " VARCHAR,"
                + PRODUCT_IMAGE + " VARCHAR,"
                + PRODUCT_CATEGORY + " VARCHAR,"
                + PRODUCT_DESCRIPTION + " VARCHAR);");
    }
    // Insert a new record into the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new record into the database
    public void insertRecord(LocalProductsModel contact) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();// use of Data add
        contentValues.put(PRODUCT_NAME, contact.getProductName());
        contentValues.put(PRODUCT_PRICE, contact.getProductPrice());
        contentValues.put(PRODUCT_QUANTITY, contact.getProductQuantity());
        contentValues.put(PRODUCT_LOCATION, contact.getProductLocation());
        contentValues.put(PRODUCT_IMAGE, contact.getProductImage());
        contentValues.put(PRODUCT_CATEGORY, contact.getProductCategory());
        contentValues.put(PRODUCT_DESCRIPTION, contact.getProductDescription());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }
    // Update an existing record in the database
    public void updateRecord(LocalProductsModel contact) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, contact.getProductName());
        contentValues.put(PRODUCT_PRICE, contact.getProductPrice());
        contentValues.put(PRODUCT_QUANTITY, contact.getProductQuantity());
        contentValues.put(PRODUCT_LOCATION, contact.getProductLocation());
        contentValues.put(PRODUCT_IMAGE, contact.getProductImage());
        contentValues.put(PRODUCT_CATEGORY, contact.getProductCategory());
        contentValues.put(PRODUCT_DESCRIPTION, contact.getProductDescription());
        Cursor cursor=null;
        try {
            // Check if the record with the given ID exists
            String selection = PRODUCT_ID + " = ?";
            String[] selectionArgs = {String.valueOf(contact.getProductId())};
            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                // Record with the ID exists, update it
                String whereClause = PRODUCT_ID + " = ?";
                String[] whereArgs = {String.valueOf(contact.getProductId())};
                database.update(TABLE_NAME, contentValues, whereClause, whereArgs);
                // Log successful update
                Log.d("Database", "Record updated successfully.");
            } else {
                // Record with the ID doesn't exist, insert it
                database.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                // Log successful insertion
                Log.d("Database", "Record inserted successfully.");
            }
        } catch (Exception e) {
            // Log any exceptions that occur
            Log.e("Database", "Error updating/inserting record: " + e.getMessage());
        } finally {
            // Close the cursor and database
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
    }


    // Delete a record from the database
    public void deleteRecord(int productId) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Check if the record with the given ID exists
            String selection = PRODUCT_ID + " = ?";
            String[] selectionArgs = {String.valueOf(productId)};

            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                // Record with the ID exists, delete it
                String whereClause = PRODUCT_ID + " = ?";
                String[] whereArgs = {String.valueOf(productId)};
                database.delete(TABLE_NAME, whereClause, whereArgs);
                // Log successful deletion
                Log.d("Database", "Record deleted successfully.");
            } else {
                // Log that the record does not exist
                Log.d("Database", "Record with ID " + productId + " does not exist.");
            }
        } catch (Exception e) {
            // Log any exceptions that occur
            Log.e("Database", "Error deleting record: " + e.getMessage());
        } finally {
            // Close the cursor and database
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
    }


    /*over alternate method */
    public ArrayList<LocalProductsModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                null, null, null, null,
                null, null);
        ArrayList<LocalProductsModel> contactModelArrayList = new ArrayList<LocalProductsModel>();
        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                LocalProductsModel  localProductsModel = new LocalProductsModel();
                localProductsModel.setProductName(cursor.getString(0));
                localProductsModel.setProductPrice(cursor.getString(1));
                localProductsModel.setProductQuantity(cursor.getString(2));
                localProductsModel.setProductLocation(cursor.getString(3));
                localProductsModel.setProductImage(cursor.getString(4));
                localProductsModel.setProductCategory(cursor.getString(5));
                localProductsModel.setProductDescription(cursor.getString(6));
                contactModelArrayList.add(localProductsModel);
            }
        }
        cursor.close();
        database.close();
        return contactModelArrayList;
    }

    // Listener interface for database events
    public interface DatabaseHelperListener{
      default void insertRecord() {
      }
      default void getAllRecords() {
      }
      void onError(String message);
  }
}