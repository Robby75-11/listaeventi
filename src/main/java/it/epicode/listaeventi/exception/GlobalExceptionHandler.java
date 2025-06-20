package it.epicode.listaeventi.exception;

import it.epicode.listaeventi.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)

    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, WebRequest request){
    ApiError apiError  = new ApiError();
    apiError.setMessage(ex.getMessage());
    apiError.setDataErrore(LocalDateTime.now());
    apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setError("Not Found");
        apiError.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setDataErrore(LocalDateTime.now());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setError("Bad Request");
        apiError.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

  

    // Gestisce tutte le altre eccezioni non catturate
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage("An unexpected error occurred: " + ex.getMessage());
        apiError.setDataErrore(LocalDateTime.now());
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setError("Internal Server Error");
        apiError.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
