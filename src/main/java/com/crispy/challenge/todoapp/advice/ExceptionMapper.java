package com.crispy.challenge.todoapp.advice;

import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(NoResultFoundException.class)
    public ProblemDetail handleException(NoResultFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("No Result Found");
        return problemDetail;
    }

}

