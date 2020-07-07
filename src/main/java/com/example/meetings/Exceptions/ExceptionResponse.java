package com.example.meetings.Exceptions;

public class ExceptionResponse {

    private String errorMessage;
    private String requestedURI;

    public ExceptionResponse(String errorMessage, String requestedURI) {
        this.errorMessage = errorMessage;
        this.requestedURI = requestedURI;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestedURI() {
        return requestedURI;
    }

    public void setRequestedURI(String requestedURI) {
        this.requestedURI = requestedURI;
    }
}
