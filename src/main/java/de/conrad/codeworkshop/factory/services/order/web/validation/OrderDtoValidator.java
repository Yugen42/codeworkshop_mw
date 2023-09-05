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

/**
 * @author Ina Lieder
 */
public class OrderDtoValidator {

    public static final int MIN_PRODUCTID_NUMBER = 100000;
    public static final int MAX_PRODUCTID_NUMBER = 999999999;
    public static final BigDecimal EXACTLY_ALLOWED_QUANTITY = new BigDecimal("42.42");

    public static void validateOrder(final @NotNull OrderDto order) {
        final Set<FieldError> validationErrors = getValidationErrorsIfExist(order);

        if (!CollectionUtils.isEmpty(validationErrors)) {
            throw new OrderValidationException("Validation reports error by the order. Please check the errors bellow for details.", validationErrors);
        }
    }

    private static Set<FieldError> getValidationErrorsIfExist(final @NotNull OrderDto order) {
        final Set<FieldError> errors = new HashSet<>();
        if (CollectionUtils.isEmpty(order.getPositions())) {
            errors.add(new FieldError("order", "order.positions", "must not be empty"));
        } else {
            errors.addAll(validatePositions(order.getPositions()));
        }
        return errors;
    }

    private static Set<FieldError> validatePositions(final List<PositionDto> positionDtoList) {
        final Set<FieldError> errors = new HashSet<>();
        IntStream.range(0, positionDtoList.size())
            .forEach(i -> {
                errors.addAll(validateProductId(positionDtoList.get(i).getProductId(), String.format("order.positions[%d].productId", i)));
                errors.addAll(validateQuantity(positionDtoList.get(i).getQuantity(), String.format("order.positions[%d].quantity", i)));
            });
        return errors;
    }

    /*
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
            errors.add(new FieldError("order.process.productId", path, "must not be empty"));
        }

        return errors;
    }

    private static boolean isWithinAllowedNumberRange(final Integer value) {
        return value >= MIN_PRODUCTID_NUMBER && value <= MAX_PRODUCTID_NUMBER;
    }

    /*
     * Quantity must match the following conditions and be either:
     * divisible by 10
     * or less than one and larger than 0
     * or exactly 42.42
     */
    private static Set<FieldError> validateQuantity(final BigDecimal quantity, final String path) {
        final Set<FieldError> errors = new HashSet<>();
        if (quantity != null) {
            if (!isDivisibleByTen(quantity) && !isEqualToAllowedQuantity(quantity) && !isLessThenOneAndLargerZero(quantity)) {
                errors.add(new FieldError("order.process.quantity", path,
                    "The value must be either divisible by 10,"
                        + " or be less than one and larger than zero"
                        + " or be exactly 42.42"));

            }
        } else {
            errors.add(new FieldError("order.process.quantity", path, "must not be empty"));
        }
        return errors;
    }

    private static boolean isDivisibleByTen(final BigDecimal value) {
        return value.remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isEqualToAllowedQuantity(final BigDecimal value) {
        return value.compareTo(EXACTLY_ALLOWED_QUANTITY) == 0;
    }

    private static boolean isLessThenOneAndLargerZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.ONE) < 0;
    }

}
