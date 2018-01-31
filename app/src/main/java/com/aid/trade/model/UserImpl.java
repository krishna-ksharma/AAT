package com.aid.trade.model;

import android.database.Cursor;

import com.aid.trade.provider.user.Users.Columns;

/**
 * Implementation of the User interface.
 */

public class UserImpl implements User {
    /**
     * The user email.
     */
    private String email;

    /**
     * The user password.
     */
    private String password;

    /**
     * The user id.
     */
    private long id;

    /**
     * Constructor.
     */
    public UserImpl() {
    }

    /**
     * Constructor.
     *
     * @param userId the id of the user
     */
    public UserImpl(long userId) {
        id = userId;
    }

    /**
     * Constructor. Initializes a new user with the data in the cursor.
     * Cursor must be compatible with the one returned from the Users provider.
     *
     * @param userId the id of the User
     * @param cursor User cursor
     */
    public UserImpl(long userId, Cursor cursor) {
        this(userId);
        init(cursor);
    }

    @Override
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Initializes the user with the values from the cursor
     *
     * @param cursor User cursor
     */
    private void init(Cursor cursor) {
        //Load values
        if (cursor != null) {
            initEmailFromCursor(cursor);
            initPasswordFromCursor(cursor);
        }
    }

    /**
     * Helper method to initialize email from cursor
     */
    private void initEmailFromCursor(Cursor cursor) {
        int index = cursor.getColumnIndex(Columns.EMAIL);
        if (index >= 0) {
            setEmail(cursor.getString(index));
        }
    }

    /**
     * Helper method to initialize password from cursor
     */
    private void initPasswordFromCursor(Cursor cursor) {
        int index = cursor.getColumnIndex(Columns.PASSWORD);
        if (index >= 0) {
            setPassword(cursor.getString(index));
        }
    }
}
