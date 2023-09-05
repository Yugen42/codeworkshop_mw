package de.conrad.codeworkshop.factory.services.order.web.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @author Ina Lieder
 */
@Data
public class OrderDto {
    private List<PositionDto> positions;

}
