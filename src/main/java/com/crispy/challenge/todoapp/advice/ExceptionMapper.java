package com.crispy.challenge.todoapp.advice;

import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {

    private final Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

    @ExceptionHandler(NoResultFoundException.class)
    public ProblemDetail handleException(NoResultFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("No Result Found.");
        logger.error(e.getClass().getSimpleName(), e);
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleException(IllegalArgumentException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Illegal Argument.");
        logger.error(e.getClass().getSimpleName(), e);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleException(ConstraintViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Constraint violation.");
        logger.error(e.getClass().getSimpleName(), e);
        return problemDetail;
    }
}
