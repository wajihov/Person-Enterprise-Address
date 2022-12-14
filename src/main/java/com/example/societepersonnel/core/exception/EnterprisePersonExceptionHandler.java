package com.example.societepersonnel.core.exception;

import com.example.societepersonnel.core.rest.ServerResponse;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class EnterprisePersonExceptionHandler {


    @ExceptionHandler(EnterprisePersonException.class)
    public ResponseEntity<ServerResponse> handleEPException(EnterprisePersonException ex) {
        ServerResponse serverResponse = ServerResponse
                .builder()
                .timeStamp(LocalDateTime.now())
                .message(ex.getCodes().getMessage())
                .build();
        return new ResponseEntity<>(serverResponse, ex.getCodes().getHttpStatus());
    }

    @ExceptionHandler(SQLGrammarException.class)
    public ResponseEntity<ServerResponse> handleSQLException(SQLGrammarException ex) {
        ServerResponse serverResponse = ServerResponse
                .builder()
                .timeStamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(serverResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ServerResponse> handleTypeMismatchException(NumberFormatException ex) {
        ServerResponse serverResponse = ServerResponse
                .builder()
                .timeStamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(serverResponse, HttpStatus.NOT_IMPLEMENTED);
    }

}
