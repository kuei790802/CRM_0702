package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailPK implements Serializable {
    private Long orderid;
    private Long productid;

    @Override
    public int hashCode() {
        return Objects.hash(orderid, productid);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderDetailPK)) return false;
        OrderDetailPK other = (OrderDetailPK)obj;

        return orderid == other.orderid && productid == other.productid;
    }
}
