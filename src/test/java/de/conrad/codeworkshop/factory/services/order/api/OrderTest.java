package de.conrad.codeworkshop.factory.services.order.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void validatePositionsIsNull() {
        // when
        order.validate();
        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    private void setOnePositionInOrder() {
        List<Position> positionList = new ArrayList<>();
        Position position = new Position();
        position.setProductId(123456);
        position.setQuantity(BigDecimal.valueOf(10));
        positionList.add(position);
        order.setPositions(positionList);
    }

    @Test
    void validateIsEmpty() {
        // given
        setOnePositionInOrder();
        // when
        order.validate();
        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateIsNotPending() {
        // given
        setOnePositionInOrder();
        order.setStatus(OrderStatus.IN_PROGRESS);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_ProductIdBetween6and9digits1() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(12345);
        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_ProductIdBetween6and9digits2() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);
        position.setQuantity(BigDecimal.valueOf(10));
        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateTask1_ProductIdBetween6and9digits3() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setQuantity(BigDecimal.valueOf(10));

        position.setProductId(123456789);

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();
        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateTask1_ProductIdBetween6and9digits4() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(1234567899);
        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_ProductIdBetween6and9digits5() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456789);
        Position position1 = new Position();
        position.setProductId(1234567899);
        positionList.add(position);
        positionList.add(position1);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_quantityDivisibleBy10_1() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(9));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_quantityDivisibleBy10_2() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(10));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateTask1_QuantityLarger0Less1_1() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(-1));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

    @Test
    void validateTask1_QuantityLarger0Less1_2() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(0.1));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateTask1_QuantityEqual42_42() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(42.42));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void validateTask1_QuantityNotEqual42_42() {
        // given
        order.setPositions(new ArrayList<>());
        List<Position> positionList = order.getPositions();
        Position position = new Position();
        position.setProductId(123456);

        position.setQuantity(BigDecimal.valueOf(52.52));

        positionList.add(position);
        order.setPositions(positionList);

        // when
        order.validate();

        // then
        assertEquals(OrderStatus.DECLINED, order.getStatus());
    }

}