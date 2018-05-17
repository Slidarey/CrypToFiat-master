package com.example.android.cryptofiat;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

public class AddCardActivity extends AppCompatActivity {

    private Spinner mFiatSpinner;

    private Spinner mCryptoSpinner;

    String mFiatValue;

    String mCryptoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        mFiatSpinner = (Spinner) findViewById(R.id.fiat_spinner);
        mCryptoSpinner = (Spinner) findViewById(R.id.cryptocurrency_spinner);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter fiatSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.fiat_currency_array, android.R.layout.simple_spinner_item);

        ArrayAdapter cryptoSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cryptocurrency_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        fiatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        cryptoSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFiatSpinner.setAdapter(fiatSpinnerAdapter);

        mCryptoSpinner.setAdapter(cryptoSpinnerAdapter);

    }

    private void savePet() {

        mFiatValue = mFiatSpinner.getSelectedItem().toString();

        mCryptoValue = mCryptoSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(mFiatValue) && TextUtils.isEmpty(mCryptoValue)) {
            return;
        }


        ContentValues values = new ContentValues();
        values.put(CurrencyEntry.COLUMN_FIATCURRENCY_NAME, mFiatValue);
        values.put(CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME, mCryptoValue);

        getContentResolver().insert(CurrencyEntry.CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option

        }
        return super.onOptionsItemSelected(item);
    }

}
