package com.Food_Donation.exception;

import com.Food_Donation.dto.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request);
    }
    @ExceptionHandler(EmptyDataException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFoundWithOk(
            EmptyDataException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(),
                HttpStatus.OK,
                request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return buildResponse("Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getMessage());
    }

@ExceptionHandler(UnauthorizedException.class)
public ResponseEntity<CustomErrorResponse> handleUnauthorized(
        UnauthorizedException ex,
        HttpServletRequest request) {

    CustomErrorResponse error = new CustomErrorResponse(
            LocalDateTime.now(),
            request.getRequestURI(),
            401,
            "Unauthorized",
            ex.getMessage()

    );

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
}
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicate(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        CustomErrorResponse error = new CustomErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setError("Conflict");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    private ResponseEntity<CustomErrorResponse> buildResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        CustomErrorResponse response = new CustomErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getRequestURI());
        response.setStatus(status.value());
        response.setError(status.getReasonPhrase());
        response.setMessage(message);

        return new ResponseEntity<>(response, status);
    }

}
