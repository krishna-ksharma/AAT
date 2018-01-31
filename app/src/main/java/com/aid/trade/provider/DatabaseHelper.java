package com.aid.trade.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Abstract database helper class to create a single database called 'AAT.db'
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * The database name.
     */
    private static final String DATABASE_NAME = "AAT.db";// AAT stands for Aid A Trade

    /**
     * The database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
