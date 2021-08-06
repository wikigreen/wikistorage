package com.wikigreen.wikistorage.rest.error.handling;

import com.wikigreen.wikistorage.security.jwt.JwtAuthenticationException;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import com.wikigreen.wikistorage.service.exceptions.InvalidPasswordException;
import com.wikigreen.wikistorage.service.exceptions.NotUniqueDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CustomErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        CustomErrorMessage errorMessage = new CustomErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                fieldErrors.stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList())
        );

        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {NotUniqueDataException.class})
    public ResponseEntity<CustomErrorMessage> handleNotUniqueDataException(NotUniqueDataException exception) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                List.of(exception.getMessage())
        );

        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomEntityNotFoundException.class})
    public ResponseEntity<CustomErrorMessage> handleEntityNotFoundException(CustomEntityNotFoundException exception){
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                List.of(exception.getFormattedMessage())
        );

        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<CustomErrorMessage> handleInvalidPasswordException(InvalidPasswordException exception){
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                List.of(exception.getMessage())
        );

        return new ResponseEntity<>(customErrorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<CustomErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException exception){
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                List.of(exception.getMessage())
        );

        return new ResponseEntity<>(customErrorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {JwtAuthenticationException.class})
    public ResponseEntity<CustomErrorMessage> handleJwtAuthenticationException(JwtAuthenticationException exception){
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                List.of(exception.getMessage())
        );

        return new ResponseEntity<>(customErrorMessage, HttpStatus.UNAUTHORIZED);
    }


}
