package com.example.meetings.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({MeetingInvalidException.class, MeetingNotFoundByTimeException.class, MeetingNotFoundByTitleException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleInvalidMeeting(final MeetingInvalidException exception,
                                                                              final HttpServletRequest request) {

        return new ExceptionResponse(exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({DbException.class, VariableInvalidException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponse handleInternalServerErrors(final Exception exception,
                                                           final HttpServletRequest request) {

        return new ExceptionResponse(exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponse handleException(final Exception exception,
                                                           final HttpServletRequest request) {

        return new ExceptionResponse(exception.getMessage(), request.getRequestURI());
    }
}
