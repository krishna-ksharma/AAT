package com.aid.trade.provider.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aid.trade.provider.DatabaseHelper;
import com.aid.trade.provider.user.Users.Columns;

/**
 * This class helps open, create, and upgrade the database file.
 */
public class UserDatabaseHelper extends DatabaseHelper {
    /**
     * The table name.
     */
    public static final String USERS_TABLE_NAME = "users";

    /**
     * The user columns has to be created.
     */
    private static final String CREATE_USERS_COLUMNS =
            Columns._ID + " INTEGER PRIMARY KEY,"
                    + Columns.EMAIL + " TEXT,"
                    + Columns.PASSWORD + " TEXT";

    /**
     * The create table statement.
     */
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + " (" + CREATE_USERS_COLUMNS + ");";

    /**
     * Constructor.
     *
     * @param context The current context
     */
    public UserDatabaseHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            onCreate(db);
        }
    }
}
