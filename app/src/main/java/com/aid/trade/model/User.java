package com.aid.trade.model;


/**
 * Defines an interface representation of a User
 */
public interface User {
    /**
     * Gets the user id.
     *
     * @return The user id
     */
    long getId();

    /**
     * Gets email.
     */
    String getEmail();

    /**
     * Sets the email
     *
     * @param email The user email
     */
    void setEmail(String email);

    /**
     * Gets password.
     */
    String getPassword();

    /**
     * Sets the password.
     *
     * @param password The user password
     */
    void setPassword(String password);
}
