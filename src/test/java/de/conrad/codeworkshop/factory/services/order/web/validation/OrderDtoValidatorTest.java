package de.conrad.codeworkshop.factory.services.order.web.validation;

import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.web.api.PositionDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Stream;

import static de.conrad.codeworkshop.factory.services.order.web.validation.OrderDtoValidator.MAX_PRODUCTID_NUMBER;
import static de.conrad.codeworkshop.factory.services.order.web.validation.OrderDtoValidator.MIN_PRODUCTID_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoValidatorTest {

    @ParameterizedTest
    @MethodSource("makeOrderAndExpectedResult")
    void validateOrderAndCheckPositionDataValidationErrorsAreCorrect(final OrderDto dto, final boolean shouldExpectValidationErrors, final String expectedValidationError) {
        if (!shouldExpectValidationErrors) {
            OrderDtoValidator.validateOrder(dto);
        } else {
            //  assertThrows(OrderValidationException.class, () -> OrderDtoValidator.validateOrder(dto));
            try {
                OrderDtoValidator.validateOrder(dto);
            } catch (final OrderValidationException e) {
                assertThat(e.getOrderValidationErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage().equals(expectedValidationError))
                    .findFirst().orElse(null))
                    .isNotNull();
            }
        }
    }

    private static Stream<Arguments> makeOrderAndExpectedResult() {
        return Stream.of(
            Arguments.of(makeOrderDto(Integer.valueOf("123456"), BigDecimal.valueOf(42.42)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("1234567"), BigDecimal.valueOf(2020)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("12345678"), BigDecimal.valueOf(0.5)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("123456789"), BigDecimal.valueOf(0.999999999)), false, null),
            Arguments.of(makeOrderDto(Integer.valueOf("44444444"), BigDecimal.valueOf(42.43)), true, "The value must be either divisible by 10, or be less than one and larger than zero or be exactly 42.42"),
            Arguments.of(makeOrderDto(Integer.valueOf("100000"), BigDecimal.valueOf(42)), true, "The value must be either divisible by 10, or be less than one and larger than zero or be exactly 42.42"),
            Arguments.of(makeOrderDto(Integer.valueOf("100000"), BigDecimal.valueOf(1)), true, "The value must be either divisible by 10, or be less than one and larger than zero or be exactly 42.42"),
            Arguments.of(makeOrderDto(Integer.valueOf("100"), BigDecimal.valueOf(1)), true, String.format("must be greater than or equal to %d and must be less than or equal to %d", MIN_PRODUCTID_NUMBER, MAX_PRODUCTID_NUMBER)),
            Arguments.of(makeOrderDto(Integer.valueOf("1234567891"), BigDecimal.valueOf(10)), true, String.format("must be greater than or equal to %d and must be less than or equal to %d", MIN_PRODUCTID_NUMBER, MAX_PRODUCTID_NUMBER))
        );
    }

    private static OrderDto makeOrderDto(final Integer productId, final BigDecimal quantity) {
        final OrderDto orderDto = new OrderDto();
        orderDto.setPositions(new ArrayList<>());
        final PositionDto positionDto = new PositionDto();
        positionDto.setProductId(productId);
        positionDto.setQuantity(quantity);
        orderDto.getPositions().add(positionDto);
        return orderDto;
    }
}