package com.example.meetings.Exceptions;

public class DbException extends RuntimeException {

    private static final String DB_ERROR = "Failed to read from / write to DB";

    public DbException() {
        super(DB_ERROR);
    }
}
