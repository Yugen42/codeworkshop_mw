package de.conrad.codeworkshop.factory.services.order.web.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Ina Lieder
 */
@Data
public class PositionDto {
    private Integer productId;
    private BigDecimal quantity;

}
