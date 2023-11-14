package com.retailease.controller;

import com.retailease.model.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<String>> constraintViolationException (ConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResponse<String>> responseStatusException (ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatus())
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    } @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<CommonResponse<String>> sQLSyntaxErrorException (SQLSyntaxErrorException exception){
        return ResponseEntity.status(500)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<String>> authenticationException (AuthenticationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<CommonResponse<String>> handleMalformedJwtException (MalformedJwtException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse<String>> handleExpiredJwtException (ExpiredJwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<CommonResponse<String>> handleUnsupportedJwtException (UnsupportedJwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<String>> handleIllegalArgumentException (IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.<String>builder().errors(exception.getMessage()).build());
    }
}
