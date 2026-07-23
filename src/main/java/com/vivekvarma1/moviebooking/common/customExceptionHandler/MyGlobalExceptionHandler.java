package com.vivekvarma1.moviebooking.common.customExceptionHandler;

import com.vivekvarma1.moviebooking.common.ApiError;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.MovieNotFoundException;
import com.vivekvarma1.moviebooking.common.response.APIResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String field =
                            ((FieldError) error).getField();

                    errors.put(
                            field,
                            error.getDefaultMessage()
                    );
                });

        Map<String, Object> response =
                new HashMap<>();

        response.put("success", false);
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse>
    handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<APIResponse>
    handleMovieNotFoundException(
            MovieNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<APIResponse>
    handleAlreadyExists(
            ResourceAlreadyExistsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<APIResponse>
    handleInvalidBookingState(
            InvalidBookingStateException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<APIResponse>
    handleApiException(
            ApiException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse>
    handleDatabaseConstraintViolation(
            DataIntegrityViolationException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                "Database constraint violation.",
                                false
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse>
    handleUnexpectedException(
            Exception ex
    ) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new APIResponse(
                                "An unexpected error occurred.",
                                false
                        )
                );
    }
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<APIResponse> handleOptimisticLock(
            ObjectOptimisticLockingFailureException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                "Sorry! One or more selected seats are no longer available. Please choose different seats and try again.",
                                false
                        )
                );
    }@ExceptionHandler(SeatAlreadyLockedException.class)
    public ResponseEntity<APIResponse> handleSeatAlreadyLocked(
            SeatAlreadyLockedException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }
    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<APIResponse> handleSeatAlreadyBooked(
            SeatAlreadyBookedException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }
    @ExceptionHandler(InvalidShowSeatException.class)
    public ResponseEntity<APIResponse> handleInvalidShowSeat(
            InvalidShowSeatException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }
    @ExceptionHandler(SeatLockExpiredException.class)
    public ResponseEntity<APIResponse> handleSeatLockExpiredException(
            SeatLockExpiredException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new APIResponse(
                                ex.getMessage(),
                                false
                        )
                );
    }
}