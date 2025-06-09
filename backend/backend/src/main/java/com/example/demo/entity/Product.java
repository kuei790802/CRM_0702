package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productid;
    private String productname;
    private String description;
    private String quantityperunit;
    private Double unitprice;
    private Boolean isactive;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantityperunit() {
        return quantityperunit;
    }

    public void setQuantityperunit(String quantityperunit) {
        this.quantityperunit = quantityperunit;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public LocalDateTime getCreateat() {
        return createat;
    }

    public void setCreateat(LocalDateTime createat) {
        this.createat = createat;
    }

    public LocalDateTime getUpdateat() {
        return updateat;
    }

    public void setUpdateat(LocalDateTime updateat) {
        this.updateat = updateat;
    }

    //--------------
    @OneToMany(mappedBy = "product")
    private List<ProductImg> productimgs;

    public List<ProductImg> getProductimgs() {
        return productimgs;
    }

    public void setProductimgs(List<ProductImg> productimgs) {
        this.productimgs = productimgs;
    }

    //-------------
    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

}
