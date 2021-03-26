package de.conrad.codeworkshop.factory.services.order.api.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ProductIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductIdConstraint {
    String message() default "Invalid product id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
