package com.aid.trade.model.exception;

/**
 * The record conflict exception.
 */
public class RecordConflictException extends Exception {
    /**
     * Constructor.
     *
     * @param message The error message
     */
    public RecordConflictException(String message) {
        super(message);
    }

}

