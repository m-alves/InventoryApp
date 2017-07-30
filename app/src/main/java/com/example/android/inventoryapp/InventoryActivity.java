package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.ItemContract.ItemEntry;

public class InventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the item data loader */
    private static final int ITEM_LOADER = 0;

    /** Adapter for the ListView */
    ItemCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the item data
        ListView itemListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of item data in the Cursor.
        mCursorAdapter = new ItemCursorAdapter(this, null);
        itemListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link DetailActivity}
                Intent intent = new Intent(InventoryActivity.this, DetailActivity.class);

                // Form the content URI that represents the specific item that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ItemEntry#CONTENT_URI}.

                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                Log.v("ListClickURI", currentItemUri.toString());

                // Set the URI on the data field of the intent
                intent.setData(currentItemUri);

                // Launch the {@link detailActivity} to display the data for the current item.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
    }

    /**
     * Helper method to insert hardcoded item data into the database. For debugging purposes only.
     */
    private void insertItem() {
        // Create a ContentValues object where column names are the keys,
        // and the item "Hammer" attributes are the values.
        String dummyImageUri = getUriStringToDrawable(this, R.drawable.dummy_item);
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Hammer");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 20);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 2);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER, "TengTools" );
        values.put(ItemEntry.COLUMN_ITEM_IMAGE, dummyImageUri );

        // Insert a new row for the dummy item into the provider using the ContentResolver.
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
    }
    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - string
     */
    public static final String getUriStringToDrawable(Context context, int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        String imageUriString = imageUri.toString();
        return imageUriString ;
    }

    /*
    * This method is called when the Sell button is clicked
    * @param currentItemUri - the Uri of the item being updated
    * @param values - the updated quantity in ContentValues object
    * @param flag - prevents that we keep receiving the Item updated toast when
    * quantity is already 0
    * */
    public void updateQuantity(Uri currentItemUri, ContentValues values, int flag){

        int rowsAffected = getContentResolver().update(currentItemUri, values, null, null);

        Integer quantity = values.getAsInteger(ItemEntry.COLUMN_ITEM_QUANTITY);
        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.detail_update_item_failed),
                    Toast.LENGTH_SHORT).show();
        } else if (values.containsKey(ItemEntry.COLUMN_ITEM_QUANTITY) && quantity > 0) {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.detail_update_item_successful),
                            Toast.LENGTH_SHORT).show();
        } else if (values.containsKey(ItemEntry.COLUMN_ITEM_QUANTITY) && flag  == 1) {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.detail_update_item_successful),
                    Toast.LENGTH_SHORT).show();
        }
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * Helper method to delete all items in the database.
     */
    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ItemEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link itemCursorAdapter} with this new cursor containing updated item data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
