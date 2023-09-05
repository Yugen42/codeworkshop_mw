package de.conrad.codeworkshop.factory.services.order.web.validation;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ina Lieder
 */
public class OrderValidationException extends RuntimeException {

    @Getter
    private Set<FieldError> orderValidationErrors;

    public OrderValidationException(final String message, final Set<FieldError> orderValidationErrors) {
        super(message);
        this.orderValidationErrors = new HashSet<>(orderValidationErrors);
    }
}
