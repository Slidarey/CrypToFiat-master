package com.example.android.cryptofiat;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Currency>> {

    public static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private static final int CURRENCY_LOADER_ID = 1;

    private TextView mEmptyView;

    private ProgressBar mProgressBar;

    private CurrencyAdapter mAdapter;

    private static final String CRYPTOCOMPARE_REQUEST_URL = "https://min-api.cryptocompare.com/data/pricemulti?" +
            "fsyms=BTC,ETH&tsyms=AED,AUD,BRL,CAD,CHF,EUR,GBP,IDR,INR,JPY,KES,KRW,NGN,PLN,RUB,THB,TRY,TZS,UAH,USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView currencyListView = (ListView) findViewById(R.id.list);

        mAdapter = new CurrencyAdapter(this, new ArrayList<Currency>());
        currencyListView.setAdapter(mAdapter);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loadManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loadManager.initLoader(CURRENCY_LOADER_ID, null, this);
        } else {
            mEmptyView = (TextView) findViewById(R.id.empty_view);
            mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            mEmptyView.setText(R.string.no_internet_connection);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<Currency>> onCreateLoader(int id, Bundle args) {
        return new CurrencyLoader(HomeActivity.this, CRYPTOCOMPARE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> data) {
        mAdapter.clear();
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mProgressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_go_to_my_cards:
                Intent intent = new Intent(HomeActivity.this, MyCardActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
