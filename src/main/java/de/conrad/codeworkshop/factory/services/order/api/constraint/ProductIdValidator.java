package de.conrad.codeworkshop.factory.services.order.api.constraint;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductIdValidator implements ConstraintValidator<ProductIdConstraint, String> {

    @Override
    public void initialize(ProductIdConstraint productId) {
    }

    @Override
    public boolean isValid(String productId, ConstraintValidatorContext cxt) {
        BigDecimal validatingValue = new BigDecimal(productId);
        return validatingValue.divide(new BigDecimal("10")).compareTo(BigDecimal.ZERO) == 0 ||
                validatingValue.compareTo(BigDecimal.ZERO) != 0 ||
                validatingValue.compareTo(new BigDecimal("42.42")) == 0;
    }
}

