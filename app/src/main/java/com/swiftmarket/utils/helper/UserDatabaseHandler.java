package com.swiftmarket.utils.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserCredentials.db";
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PROFILE_IMAGE = "profile_image";

    private SQLiteDatabase database;

    // Constructor
    public UserDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_PROFILE_IMAGE + " TEXT);");
    }

    // Upgrade the database (if needed)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert user credentials into the database
    public void insertUser(String email, String password, String profileImage) {
        database = this.getWritableDatabase();

        // Check if the email already exists in the database
        if (!isEmailExists(email)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_PROFILE_IMAGE, profileImage);
            database.insert(TABLE_NAME, null, contentValues);
        } else {
            // Log that the email already exists
            Log.d("UserDatabase", "User with email " + email + " already exists.");
        }

        database.close();
    }


    // Check if the email already exists in the database
    private boolean isEmailExists(String email) {
        String[] columns = {COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = database.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        boolean emailExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return emailExists;
    }

    // Retrieve user credentials from the database
    public Cursor getUser(String email, String password) {
        database = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_PROFILE_IMAGE};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        return database.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    // Retrieve user profile image from the database based on email
    @SuppressLint("Range")
    public String getProfileImage(String email) {
        database = this.getReadableDatabase();
        String[] columns = {COLUMN_PROFILE_IMAGE};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = database.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String profileImage = null;
        if (cursor.moveToFirst()) {
            profileImage = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_IMAGE));
        }
        cursor.close();
        database.close();
        return profileImage;
    }

    // Listener interface for database events
    public interface UserDatabaseListener {
        void onError(String message);
    }
}
