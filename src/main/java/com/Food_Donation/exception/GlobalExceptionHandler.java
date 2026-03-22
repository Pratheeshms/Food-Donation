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

//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public Map<String, Object> handleUnauthorized(UnauthorizedException ex) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("message", ex.getMessage());
//        error.put("status", 401);
//        error.put("time", LocalDateTime.now());
//        return error;
//    }
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
