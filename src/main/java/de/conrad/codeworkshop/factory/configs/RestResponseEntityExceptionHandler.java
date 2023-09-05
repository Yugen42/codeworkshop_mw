package de.conrad.codeworkshop.factory.configs;

import de.conrad.codeworkshop.factory.services.order.web.validation.OrderValidationException;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ina Lieder
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @Primary
    public ResponseEntity handleMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException ex) {
        final String bodyOfResponse = "Payload not readable or empty";
        return new ResponseEntity(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {OrderValidationException.class})
    protected ResponseEntity<Object> handleOrderValidationException(final OrderValidationException ex,
                                                                    final WebRequest request) {
        final List<String> violations = ex.getOrderValidationErrors().stream().map(this::createViolation).collect(Collectors.toList());
        final String bodyOfResponse = ex.getMessage() + String.join(",", violations);

        return new ResponseEntity(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    private String createViolation(final FieldError fieldError) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" {\n");
        sb.append("    field: ").append(fieldError.getField()).append("\n");
        sb.append("    error: ").append(fieldError.getDefaultMessage()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
