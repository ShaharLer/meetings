package com.example.meetings.Exceptions;

public class NullListException extends VariableInvalidException {

    public static final String NULL_LIST_ERROR = "Failed due to null list in DB";

    public NullListException() {
        super(NULL_LIST_ERROR);
    }
}
