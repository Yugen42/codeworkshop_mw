package de.conrad.codeworkshop.factory.services.order.api;

import de.conrad.codeworkshop.factory.services.order.api.constraint.ProductIdConstraint;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;

/**
 * @author Andreas Hartmann
 */
public class Position {
    @Length(min = 6, max = 9)
    @ProductIdConstraint()
    private Integer productId;
    private BigDecimal quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
