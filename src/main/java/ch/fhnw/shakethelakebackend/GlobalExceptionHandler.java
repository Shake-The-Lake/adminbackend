package ch.fhnw.shakethelakebackend;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles exceptions that are thrown by the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the MethodArgumentNotValidException and returns a response entity with the errors.
     * @param ex the exception that is thrown
     * @return a response entity with the errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the HttpMessageNotReadableException and returns a response entity with the error message.
     * @param e the exception that is thrown
     * @return a response entity with the error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the EntityNotFoundException and returns a response entity with the error message.
     * @param e the exception that is thrown
     * @return a response entity with the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the IllegalArgumentException and returns a response entity with the error message.
     * @param e the exception that is thrown
     * @return a response entity with the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the DataIntegrityViolationException and returns a response entity with the error message.
     * @param ex the exception that is thrown
     * @return a response entity with the error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return new ResponseEntity<>("Entity is still referenced by another table", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(ex.getMostSpecificCause().getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles the Exception and returns a response entity with the error message.
     * @param ex the exception that is thrown
     * @return a response entity with the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalErrors(Exception ex) {
        System.out.println(ex.getMessage()); //TODO: replace with logging
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
