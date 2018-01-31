package com.aid.trade.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.aid.trade.model.exception.RecordConflictException;
import com.aid.trade.provider.user.Users.Columns;
import com.aid.trade.provider.user.UsersProvider;

/**
 * The user model.
 */

public class UserModel {
    /**
     * Projection used to load Users
     */
    private static final String PROJECTION[] = new String[]{
            Columns._ID, Columns.EMAIL, Columns.PASSWORD
    };

    /**
     * Creates the specified user to persistent storage
     *
     * @param context current context
     * @param user    the user instance to be saved
     * @return return the uri to the user, or null if the operation fails.
     */
    public static Uri createUser(Context context, User user) throws RecordConflictException {
        if (findUserByEmail(context, user.getEmail()) != null) {
            throw new RecordConflictException("Email already taken.");
        }
        final ContentValues values = new ContentValues();
        values.put(Columns.EMAIL, user.getEmail());
        values.put(Columns.PASSWORD, user.getPassword());
        return context.getContentResolver().insert(UsersProvider.CONTENT_URI, values);
    }

    /**
     * Finds user by email
     *
     * @param context current context
     * @param email   the user email
     * @return The user
     */
    public static User findUserByEmail(Context context, String email) {
        User user = null;
        String selection = Columns.EMAIL + " =?";
        String[] selectionArgs = {email};
        Cursor cursor = context.getContentResolver().query(UsersProvider.CONTENT_URI, PROJECTION, selection,
                selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(Columns._ID);
            user = new UserImpl(cursor.getLong(index), cursor);
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }

    /**
     * Returns TRUE if specified user exists
     *
     * @param context current context
     * @param user    the user
     * @return Returns TRUE if user exists else FALSE
     */
    public static boolean exists(Context context, User user) {
        String selection = Columns.EMAIL + " =?" + " AND " + Columns.PASSWORD + " =?";
        String[] selectionArgs = {user.getEmail(), user.getPassword()};
        Cursor cursor = context.getContentResolver().query(UsersProvider.CONTENT_URI, PROJECTION, selection,
                selectionArgs, null);

        boolean exists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

}
