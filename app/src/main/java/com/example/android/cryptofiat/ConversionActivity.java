package com.example.android.cryptofiat;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class ConversionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrencyUri;
    private int CURRENCY_LOADER = 2;
    private EditText mCryptoEditText;
    private TextView mCryptoTextView;
    private TextView mFiatTextValue;
    private TextView mFiatTextView;
    private Button mConvertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mFiatTextView = (TextView) findViewById(R.id.fiat_text_view);
        mFiatTextValue = (TextView) findViewById(R.id.fiat_text_value);
        mCryptoTextView = (TextView) findViewById(R.id.crypto_text_view);
        mCryptoEditText = (EditText) findViewById(R.id.crypto_edit_text);
        mConvertButton = (Button) findViewById(R.id.covert_button);

        mFiatTextValue.setText(extractCoversionValues() + "");

        mConvertButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                coversion();

            }
        });


        Intent intent = getIntent();
        mCurrencyUri = intent.getData();

        getLoaderManager().initLoader(CURRENCY_LOADER, null, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //In this projection, we are only passing the columns we need in the UI
        //And the _ID is compulsory for the cursor you want to pass to the cursor adapter
        String[] projection = {CurrencyEntry._ID,
                CurrencyEntry.COLUMN_FIATCURRENCY_NAME,
                CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME};

        return new CursorLoader(this, mCurrencyUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Update the PetCursorAdapter with this new cursor containing updated pet data
        if (data == null || data.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        //Even though it has only one item,it starts from position -1
        if (data.moveToFirst()) {

            // Find the columns of pet attributes that we're interested in
            int fiatColumnIndex = data.getColumnIndexOrThrow(CurrencyEntry.COLUMN_FIATCURRENCY_NAME);
            int cryptoColumnIndex = data.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME);

            // Extract out the value from the Cursor for the given column index
            String fiatValue = data.getString(fiatColumnIndex);
            String cryptoValue = data.getString(cryptoColumnIndex);

            // Update the views on the screen with the values from the database
            mFiatTextView.setText(fiatValue);
            mCryptoTextView.setText(cryptoValue);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when data is needed to be deleted

    }

    public double extractCoversionValues(){

        String mCryptoString = mCryptoTextView.getText().toString();
        String mFiatString = mFiatTextView.getText().toString();

        String CONVERSION_URL="https://min-api.cryptocompare.com/data/price?fsym=" + mCryptoString + "&tsyms=" + mFiatString;

        String myJsonResponse = CurrencyUtils.fetchConversionCurrencyData(CONVERSION_URL);

        if (TextUtils.isEmpty(myJsonResponse)) {
            //it is better to return Collection.emptyList() than to return null in this case
            return 0;
        }

        double conversionValue = 0;
        // Create an empty ArrayList that we can start adding currency data to


        // Try to parse the JSON response String. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseResponseObject = new JSONObject(myJsonResponse);
            conversionValue = baseResponseObject.getDouble(mFiatTextView.getText().toString());

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing t89+659he earthquake JSON results", e);
        }

        // Return the list of currencies
        return conversionValue;
    }

    public void coversion(){
        double conversionValue =  extractCoversionValues();
        double userValue = Double.parseDouble(mCryptoEditText.getText().toString());
        double result = userValue * conversionValue;
        mFiatTextValue.setText(String.valueOf(result));
    }

}
