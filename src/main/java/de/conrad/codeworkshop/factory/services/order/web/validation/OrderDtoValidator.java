package de.conrad.codeworkshop.factory.services.order.web.validation;

import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.web.api.PositionDto;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber.MAX_PRODUCTID_NUMBER;
import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber.MIN_PRODUCTID_NUMBER;

/**
 * @author Ina Lieder
 */
public class OrderDtoValidator {

    public static final BigDecimal EXACTLY_ALLOWED_QUANTITY = new BigDecimal("42.42");

    public static Set<FieldError> validateOrder(final @NotNull OrderDto order) {
        final Set<FieldError> validationErrors = new HashSet<>();
        validationErrors.addAll(validatePositions(order.getPositions()));
        return validationErrors;
    }

    private static Set<FieldError> validatePositions(final List<PositionDto> positionDtoList) {
        final Set<FieldError> errors = new HashSet<>();
        if (CollectionUtils.isEmpty(positionDtoList)) {
            errors.add(new FieldError("order", "positions", "must not be empty"));
            return errors;
        }
        IntStream.range(0, positionDtoList.size())
            .forEach(i -> {
                errors.addAll(validateProductId(positionDtoList.get(i).getProductId(), String.format("positions[%d].productId", i)));
                errors.addAll(validateQuantity(positionDtoList.get(i).getQuantity(), String.format("positions[%d].quantity", i)));
            });
        return errors;
    }

    /**
     * Product must match the following conditions and be:
     * between given range 100000 and 999999999
     */
    private static Set<FieldError> validateProductId(final Integer productId, final String path) {
        final Set<FieldError> errors = new HashSet<>();
        if (productId != null) {
            if (!isWithinAllowedNumberRange(productId)) {
                errors.add(new FieldError(productId.toString(), path, String.format("must be greater than or equal "
                    + "to %d and must be less than or equal to %d", MIN_PRODUCTID_NUMBER, MAX_PRODUCTID_NUMBER)));
            }
        } else {
            errors.add(new FieldError("process.productId", path, "must not be empty"));
        }
        return errors;
    }

    private static boolean isWithinAllowedNumberRange(final Integer value) {
        return value >= MIN_PRODUCTID_NUMBER && value <= MAX_PRODUCTID_NUMBER;
    }

    /**
     * Quantity must match the following conditions and be either:
     * divisible by 10
     * or less than one and larger than 0
     * or exactly 42.42
     */
    private static Set<FieldError> validateQuantity(final BigDecimal quantity, final String path) {
        final Set<FieldError> errors = new HashSet<>();
        if (quantity != null) {
            if (!isDivisibleByTen(quantity) && !isEqualToAllowedQuantity(quantity) && !isLessThenOneAndLargerZero(quantity)) {
                errors.add(new FieldError("process.quantity", path,
                    "The value must be either divisible by 10,"
                        + " or be less than one and larger than zero"
                        + " or be exactly 42.42"));

            }
        } else {
            errors.add(new FieldError("process.quantity", path, "must not be empty"));
        }
        return errors;
    }

    private static boolean isDivisibleByTen(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        return value.remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isEqualToAllowedQuantity(final BigDecimal value) {
        return value.compareTo(EXACTLY_ALLOWED_QUANTITY) == 0;
    }

    private static boolean isLessThenOneAndLargerZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.ONE) < 0;
    }

}
