package de.conrad.codeworkshop.factory.services.order.web.mapping;

import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber;
import de.conrad.codeworkshop.factory.services.order.business.domain.Position;
import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.web.api.PositionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoToDomainMapper {

    public Order orderDtoToDomain(final OrderDto orderDto) {
        final Order order = new Order();
        order.setPositions(positionsDtoToDomain(orderDto.getPositions()));
        return order;
    }

    private List<Position> positionsDtoToDomain(final List<PositionDto> positionDtos) {
        final List<Position> positions = new ArrayList<>();
        for (PositionDto positionDto : positionDtos) {
            positions.add(positionDtoToDomain(positionDto));
        }
        return positions;
    }

    private Position positionDtoToDomain(final PositionDto dto) {
        final Position position = new Position();
        position.setProductId(dto.getProductId());
        position.setQuantity(dto.getQuantity());
        return position;
    }

}
