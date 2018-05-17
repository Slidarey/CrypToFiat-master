package com.example.android.cryptofiat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

/**
 * Created by Zahir on 12/11/2017.
 */

public class CurrencyCursorAdapter extends CursorAdapter {


    public CurrencyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.card_list, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this method
        TextView fiatCurrencyTextView = (TextView) view.findViewById(R.id.fiat_currency);
        TextView cryptocurrencyTextView = (TextView) view.findViewById(R.id.cryptocurrency);

        int fiatCurrencyColumnIndex = cursor.getColumnIndexOrThrow(CurrencyEntry.COLUMN_FIATCURRENCY_NAME);
        int cryptocurrencyColumnIndex = cursor.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME);

        String fiatCurrencyTv = cursor.getString(fiatCurrencyColumnIndex);
        String cryptocurrencyTv = cursor.getString(cryptocurrencyColumnIndex);

        fiatCurrencyTextView.setText(fiatCurrencyTv);
        cryptocurrencyTextView.setText(cryptocurrencyTv);


    }

}
