package com.example.meetings.Exceptions;

public class InvalidListIndexException extends VariableInvalidException {

    public static final String INVALID_INDEX_ERROR = "Failed due to invalid index in list";

    public InvalidListIndexException() {
        super(INVALID_INDEX_ERROR);
    }
}
