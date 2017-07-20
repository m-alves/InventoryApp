package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.ItemContract.ItemEntry;

/**
 * Created by Utilizador on 15/07/2017.
 */

public class ItemCursorAdapter extends CursorAdapter {

    //private View.OnClickListener onSellClickListener;

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */

    Context mContext;
    public ItemCursorAdapter(Context context, Cursor c) {

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
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
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
    public void bindView(View view, Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name_inventory);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_inventory);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_inventory);
        final Button sellButton = (Button) view.findViewById(R.id.sell_button);
        sellButton.setTag(cursor.getPosition());
        mContext = context;
        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

        // Read the pet attributes from the Cursor for the current pet
        String itemName = cursor.getString(nameColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);
        String itemQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(itemName);
        priceTextView.setText(itemPrice);
        quantityTextView.setText(itemQuantity);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Clicked", "Yes");
                int position = (int) sellButton.getTag();
                cursor.moveToPosition(position);
                long id = cursor.getLong(cursor.getColumnIndex(ItemEntry._ID));
                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                Log.v("URI", currentItemUri.toString());
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                if(quantity > 0){
                    quantity -= 1;
                } else {
                    Toast.makeText(mContext,  mContext.getResources().getString(R.string.negative_quantity_alert),
                            Toast.LENGTH_SHORT).show();
                }
                ContentValues values = new ContentValues();
                values.put(ItemEntry.COLUMN_ITEM_QUANTITY, new Integer(quantity).toString());
                Log.v("Quantity String", new Integer(quantity).toString());
                ((InventoryActivity) v.getContext()).updateQuantity(currentItemUri, values);

            }
        });

        //sellButton.setOnClickListener(this.onSellClickListener);

    }

   /* public void setOnSellClickListener(final View.OnClickListener onClickListener) {
        this.onSellClickListener = onClickListener;
    }*/


}




