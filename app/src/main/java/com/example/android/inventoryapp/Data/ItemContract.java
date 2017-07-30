package com.example.android.inventoryapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Utilizador on 15/07/2017.
 */

public class ItemContract {

    // Empty constructor.
    private ItemContract() {}

    /**
     * The "Content authority" definition
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    /**
     * Using CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_ITEMS = "items";

    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single item.
     */
    public static final class ItemEntry implements BaseColumns {

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /** Name of database table for items */
        public final static String TABLE_NAME = "items";

        /**
         * Unique ID number for the item (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_NAME ="name";

        /**
         * Price of the item.
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_PRICE = "price";

        /**
         * Quantity of the item.
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_QUANTITY = "quantity";

        /**
         * Supplier of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_SUPPLIER = "supplier";

        /**
         * URI for the image of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_IMAGE = "image";
    }

}




