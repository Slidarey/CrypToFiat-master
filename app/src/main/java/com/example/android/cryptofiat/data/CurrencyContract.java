package com.example.android.cryptofiat.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Zahir on 12/11/2017.
 */

public class CurrencyContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.cryptofiat";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private CurrencyContract(){
    }

    public static final class CurrencyEntry implements BaseColumns {

        public static final String TABLE_NAME = "currency";

        //the id comes from the BaseColumns class
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIATCURRENCY_NAME = "fiatcurrency";
        public static final String COLUMN_CRYPTOCURRENCY_NAME = "cryptocurrency";


        public static final String PATH_CURRENCY = "currency";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CURRENCY);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + CONTENT_AUTHORITY + "/" + PATH_CURRENCY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + CONTENT_AUTHORITY + "/" + PATH_CURRENCY;
    }
}
