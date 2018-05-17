package com.example.android.cryptofiat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

/**
 * Created by Zahir on 12/11/2017.
 */

public class CurrencyDbHelper extends SQLiteOpenHelper {


    private static final String COMMA = ",";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + CurrencyEntry.TABLE_NAME + " ("
            + CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA
            + CurrencyEntry.COLUMN_FIATCURRENCY_NAME + " TEXT NOT NULL" + COMMA
            + CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME + " TEXT NOT NULL)";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + CurrencyEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Currency.db";

    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
