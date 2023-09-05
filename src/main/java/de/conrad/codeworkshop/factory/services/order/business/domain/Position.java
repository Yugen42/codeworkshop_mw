package de.conrad.codeworkshop.factory.services.order.business.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Andreas Hartmann
 */
@Data
public class Position {
    private Integer productId;
    private BigDecimal quantity;

}
