package com.example.android.cryptofiat.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

/**
 * Created by Zahir on 12/11/2017.
 */

public class CurrencyProvider extends ContentProvider {

    private static final int CURRENCY = 100;
    private static final int CURRENCY_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer. this is run the first time anything is called from this class
    static {
        sUriMatcher.addURI(CurrencyContract.CONTENT_AUTHORITY, CurrencyEntry.PATH_CURRENCY, CURRENCY);
        sUriMatcher.addURI(CurrencyContract.CONTENT_AUTHORITY, CurrencyEntry.PATH_CURRENCY + "/#", CURRENCY_ID);
    }

    public static final String LOG_TAG = CurrencyProvider.class.getSimpleName();

    /*Database helper object*/
    private CurrencyDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        //this line of code is to get access to the database
        mDbHelper = new CurrencyDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(CurrencyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CURRENCY_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = CurrencyEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(CurrencyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        //Set Notification URI on the cursor,
        //So we know what content URI the cursor was created for
        //If the data at this URI changes, then we know we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY:
                return insertCurrency(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for" + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    @Nullable
    private Uri insertCurrency(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(CurrencyEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowDeleted;
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY:
                // Delete all rows that match the selection and selection args
                rowDeleted = database.delete(CurrencyEntry.TABLE_NAME, selection, selectionArgs);
                if(rowDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;
            case CURRENCY_ID:
                // Delete a single row given by the ID in the URI
                selection = CurrencyEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = database.delete(CurrencyEntry.TABLE_NAME, selection, selectionArgs);
                if(rowDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     * The purpose of this method is to return a String that describes the type of the data stored at the input Uri.
     * This String is known as the MIME type, which can also be referred to as content type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY:
                return CurrencyEntry.CONTENT_LIST_TYPE;
            case CURRENCY_ID:
                return CurrencyEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
