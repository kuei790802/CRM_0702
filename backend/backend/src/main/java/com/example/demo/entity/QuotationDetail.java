package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quotationdetail")
public class QuotationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotationdetailid")
    private Long id;

    @Column(name = "quotationid")
    private Long quotationid;

    @Column(name = "productid")
    private Long productid;

    @Column(name = "quotationdetailquantity")
    private Long qty;

    @Column(name = "quotationdetailunitprice")
    private Double unitPrice;

    @Column(name = "quotationdetaildiscount")
    private Double discount;

    @Column(name = "subtotal")
    private Double subtotal;

    // Getter and Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuotationid() {
        return quotationid;
    }

    public void setQuotationid(Long quotationid) {
        this.quotationid = quotationid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
