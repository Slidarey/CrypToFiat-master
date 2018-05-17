package com.example.android.cryptofiat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zahir on 11/11/2017.
 */

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    public CurrencyAdapter (Context context, ArrayList<Currency> currency){
        super(context, 0, currency);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Currency currentCurrency = getItem(position);

        TextView btcValue = (TextView) listItemView.findViewById(R.id.btc_value);
        btcValue.setText(String.valueOf(currentCurrency.getValueOfBTC()));

        TextView currencyDescription = (TextView) listItemView.findViewById(R.id.currency_description);
        currencyDescription.setText(currentCurrency.getCurrencyDescription());

        TextView ethValue = (TextView) listItemView.findViewById(R.id.eth_value);
        ethValue.setText(String.valueOf(currentCurrency.getValueOfETH()));

        return listItemView;
    }
}
