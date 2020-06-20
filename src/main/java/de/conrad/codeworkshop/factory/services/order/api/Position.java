package de.conrad.codeworkshop.factory.services.order.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Andreas Hartmann
 */
public class Position {
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

    public boolean isProductIdValid(){
        return String.valueOf(this.getProductId()).length() >= 6 && String.valueOf(this.getProductId()).length() <= 9;
    }

    public boolean isQuantityValid(){
        if (quantity.stripTrailingZeros().scale() < 0){
                return true;
        }

        if (quantity.min(BigDecimal.ONE).equals(quantity)
                && quantity.max(BigDecimal.ZERO).equals(quantity)
                || quantity.compareTo(BigDecimal.valueOf(42.42)) == 0){
            return true;
        }

        return false;
    }
}
