package com.tup.textilapp.exception;

import com.mercadopago.exceptions.MPException;
import com.tup.textilapp.exception.custom.MPApiRuntimeException;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(403).body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(MPApiRuntimeException.class)
    public ResponseEntity<?> handleMPApiRuntimeException(MPApiRuntimeException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
    @ExceptionHandler(MPException.class)
    public ResponseEntity<?> handleMPException(MPException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(401).body(new ResponseMessageDTO(e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.toString());
        return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
    }

}
