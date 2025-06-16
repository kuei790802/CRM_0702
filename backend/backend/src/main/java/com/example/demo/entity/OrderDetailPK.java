// OrderDetailPK.java
package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailPK implements Serializable {
    // ★★★ 修正點 2：屬性名稱必須與 Entity 中的 @Id 屬性名稱完全一致 ★★★
    private Long order;   // 從 orderid 改為 order
    private Long product; // 從 productid 改為 product

    // Getters and Setters (如果需要的話)

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderDetailPK)) return false;
        OrderDetailPK other = (OrderDetailPK)obj;
        return Objects.equals(order, other.order) && Objects.equals(product, other.product);
    }
}