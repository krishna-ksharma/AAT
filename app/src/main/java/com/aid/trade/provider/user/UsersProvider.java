package com.aid.trade.provider.user;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aid.trade.provider.DatabaseHelper;
import com.aid.trade.provider.user.Users.Columns;

import java.util.HashMap;

/**
 * The user provider.
 */

public class UsersProvider extends ContentProvider {
    /**
     * The authority for the users provider
     */
    public static final String AUTHORITY = "com.aid.users.provider.Users";

    /**
     * The content:// style URL for user table
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of users.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aid.user";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single user.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aid.user";

    /**
     * Projection map for queries to the database
     */
    private static HashMap<String, String> sUsersProjectionMap;

    /**
     * Code for the users that is used to load all users
     */
    private static final int USERS = 1;

    /**
     * Code for the user id that is used to load a single user
     */
    private static final int USER_ID = 2;

    /**
     * Helper class to access database
     */
    private DatabaseHelper databaseHelper;

    /**
     * Static uri matcher
     */
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "users", USERS);
        sUriMatcher.addURI(AUTHORITY, "users/#", USER_ID);

        sUsersProjectionMap = new HashMap<String, String>();
        sUsersProjectionMap.put(Columns._ID, Columns._ID);
        sUsersProjectionMap.put(Columns.EMAIL, Columns.EMAIL);
        sUsersProjectionMap.put(Columns.PASSWORD, Columns.PASSWORD);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new UserDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                return CONTENT_TYPE;

            case USER_ID:
                return CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);

        switch (sUriMatcher.match(uri)) {
            case USERS:
                qb.setProjectionMap(sUsersProjectionMap);
                break;

            case USER_ID:
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(Columns._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and run the query
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data
        // changes
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (sUriMatcher.match(uri) == USERS) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(UserDatabaseHelper.USERS_TABLE_NAME, null, values);
            if (rowId >= 0) {
                Uri userUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
                getContext().getContentResolver().notifyChange(userUri, null);
                return userUri;
            }
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.delete(UserDatabaseHelper.USERS_TABLE_NAME, where, whereArgs);
                break;

            case USER_ID:
                String userId = uri.getPathSegments().get(1);
                count = db.delete(UserDatabaseHelper.USERS_TABLE_NAME,
                        Columns._ID + "=" + userId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.update(UserDatabaseHelper.USERS_TABLE_NAME, values, where, whereArgs);
                break;

            case USER_ID:
                String userId = uri.getPathSegments().get(1);
                count = db.update(UserDatabaseHelper.USERS_TABLE_NAME, values, Columns._ID + "=" + userId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Decode a content URL into the table, projection, and arguments used to
     * access the corresponding database rows.
     */
    private static class SqlArguments {
        public String table;

        /**
         * Operate on existing rows.
         */
        public SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = url.getPathSegments().get(0);
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where)) {
                throw new UnsupportedOperationException("WHERE clause not supported: " + url);
            } else {
                this.table = url.getPathSegments().get(0);
            }
        }
    }
}
