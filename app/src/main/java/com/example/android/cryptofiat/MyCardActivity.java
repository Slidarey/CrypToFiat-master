package com.example.android.cryptofiat;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.cryptofiat.data.CurrencyContract.CurrencyEntry;

import static android.R.attr.id;

public class MyCardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int URL_LOADER = 0;
    CurrencyCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCardActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });

        ListView currencyListView = (ListView) findViewById(R.id.card_view_pet);

        currencyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri currentCurrencyUri = ContentUris.withAppendedId(CurrencyEntry.CONTENT_URI, id);
                showDeleteConfirmationDialog(currentCurrencyUri);
                return false;
            }
        });

        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyCardActivity.this, ConversionActivity.class);
                Uri currentCurrencyUri = ContentUris.withAppendedId(CurrencyEntry.CONTENT_URI, id);
                intent.setData(currentCurrencyUri);
                startActivity(intent);

            }
        });

        mCursorAdapter = new CurrencyCursorAdapter(this, null);
        currencyListView.setAdapter(mCursorAdapter);

        //Kick off the loader
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //In this projection, we are only passing the columns we need in the UI
        //And the _ID is compulsory for the cursor you want to pass to the cursor adapter
        String[] projection = {CurrencyEntry._ID,
                CurrencyEntry.COLUMN_FIATCURRENCY_NAME,
                CurrencyEntry.COLUMN_CRYPTOCURRENCY_NAME};

        return new CursorLoader(this, CurrencyEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Update the PetCursorAdapter with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when data is needed to be deleted
        mCursorAdapter.swapCursor(null);
    }
    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteCard(Uri uri) {
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    private void showDeleteConfirmationDialog( final Uri uri) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteCard(uri);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
