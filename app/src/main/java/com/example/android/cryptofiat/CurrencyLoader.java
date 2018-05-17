package com.example.android.cryptofiat;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Zahir on 11/11/2017.
 */

public class CurrencyLoader extends AsyncTaskLoader<List<Currency>> {

    public static final String LOG_TAG = CurrencyLoader.class.getSimpleName();

    private String mUrl;

    public CurrencyLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Currency> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Currency> earthquakes = CurrencyUtils.fetchCurrencyData(mUrl);
        return earthquakes;

    }
}
