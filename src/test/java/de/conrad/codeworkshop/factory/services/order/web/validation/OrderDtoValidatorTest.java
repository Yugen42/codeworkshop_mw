package de.conrad.codeworkshop.factory.services.order.web.validation;

import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.web.api.PositionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber.MAX_PRODUCTID_NUMBER;
import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber.MIN_PRODUCTID_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoValidatorTest {

    public static final String QUANTITY_VALIDATION_ERRORMESSAGE = "The value must be either divisible by 10, or be less than one and larger than zero or be exactly 42.42";
    public static final String POSITION_ERROR_MESSAGE = "must be greater than or equal to %d and must be less than or equal to %d";
    public static final String MUST_NOT_BE_EMPTY_ERROR_MESSAGE = "must not be empty";

    @ParameterizedTest
    @MethodSource("makeOrderAndExpectedResult")
    void validateOrderAndCheckPositionDataValidationErrorsAreCorrect(final OrderDto dto, final boolean shouldExpectValidationErrors, final String expectedValidationError) {
        if (!shouldExpectValidationErrors) {
            final Set<FieldError> errors = OrderDtoValidator.validateOrder(dto);
            assertThat(CollectionUtils.isEmpty(errors)).isTrue();
        } else {
            final Set<FieldError> errors = OrderDtoValidator.validateOrder(dto);
            assertThat(errors.size()).isEqualTo(1);
            assertThat(errors.stream()
                .filter(fieldError -> fieldError.getDefaultMessage().equals(expectedValidationError))
                .findFirst().orElse(null))
                .isNotNull();
        }
    }

    @Test
    void validateOrderWithManyViolations() {
        final OrderDto dto = makeOrderDto(-10, BigDecimal.valueOf(43));
        dto.getPositions().add(makePositionDto(null, BigDecimal.valueOf(-1)));
        final Set<FieldError> errors = OrderDtoValidator.validateOrder(dto);
        assertThat(errors.size()).isEqualTo(4);
    }

    private static Stream<Arguments> makeOrderAndExpectedResult() {
        return Stream.of(
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(42.42)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("1234567"), BigDecimal.valueOf(2020)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("12345678"), BigDecimal.valueOf(0.5)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("123456789"), BigDecimal.valueOf(0.999999999)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(0)), true, QUANTITY_VALIDATION_ERRORMESSAGE),
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(42.43)), true, QUANTITY_VALIDATION_ERRORMESSAGE),
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(1)), true, QUANTITY_VALIDATION_ERRORMESSAGE),
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(-1L)), true, QUANTITY_VALIDATION_ERRORMESSAGE),
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), null), true, MUST_NOT_BE_EMPTY_ERROR_MESSAGE),
            Arguments.of(makeOrderDto(Integer.valueOf("1"), BigDecimal.valueOf(42.42)), true, String.format(POSITION_ERROR_MESSAGE, MIN_PRODUCTID_NUMBER, MAX_PRODUCTID_NUMBER)),
            Arguments.of(makeOrderDto(Integer.valueOf("-1"), BigDecimal.valueOf(10)), true, String.format(POSITION_ERROR_MESSAGE, MIN_PRODUCTID_NUMBER, MAX_PRODUCTID_NUMBER)),
            Arguments.of(makeOrderDto(null, BigDecimal.valueOf(10)), true, MUST_NOT_BE_EMPTY_ERROR_MESSAGE),
            Arguments.of(new OrderDto(), true, MUST_NOT_BE_EMPTY_ERROR_MESSAGE)
        );
    }

    private static OrderDto makeOrderDto(final Integer productId, final BigDecimal quantity) {
        final OrderDto orderDto = new OrderDto();
        orderDto.setPositions(new ArrayList<>());
        orderDto.getPositions().add(makePositionDto(productId, quantity));
        return orderDto;
    }

    private static PositionDto makePositionDto(final Integer productId, final BigDecimal quantity) {
        final PositionDto positionDto = new PositionDto();
        positionDto.setProductId(productId);
        positionDto.setQuantity(quantity);
        return positionDto;
    }
}