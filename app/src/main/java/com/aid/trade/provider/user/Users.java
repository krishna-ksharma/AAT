package com.aid.trade.provider.user;

import android.provider.BaseColumns;

/**
 * Defines the public Users content provider API columns
 */
public class Users {
    public static final class Columns implements BaseColumns {
        /**
         * Private Constructor.
         */
        private Columns() {
        }

        /**
         * The email column.
         */
        public static final String EMAIL = "email";
        /**
         * The password column.
         */
        public static final String PASSWORD = "password";
    }
}
