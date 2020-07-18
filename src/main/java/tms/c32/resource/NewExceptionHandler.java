package tms.c32.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tms.c32.entity.ApiResponse;

import javax.el.MethodNotFoundException;

@ControllerAdvice
public class NewExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ApiResponse> NewMethodArgumentTypeMismatchException (){
        return new ResponseEntity<>(new ApiResponse (HttpStatus.BAD_REQUEST.value(), "pet", "Invalid ID supplied"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (MethodNotFoundException.class)
    public final ResponseEntity<ApiResponse> NewMethodNotFoundException (MethodNotFoundException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(new ApiResponse (HttpStatus.METHOD_NOT_ALLOWED.value(), "pet", message), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler (NullPointerException .class)
    public final ResponseEntity<ApiResponse> NewNullPointerException (NullPointerException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(new ApiResponse (HttpStatus.NOT_FOUND.value(), "pet", message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public final ResponseEntity<ApiResponse> NewIllegalArgumentException (IllegalArgumentException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(new ApiResponse (HttpStatus.BAD_REQUEST.value(), "pet", message), HttpStatus.BAD_REQUEST);
    }
}
