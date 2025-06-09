package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryid;
    private String location;
    private Integer unitsinstock;
    private Integer unitsinreserved;
    private LocalDate lastrestockdate;
    private LocalDateTime updateat;

    public Long getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(Long inventoryid) {
        this.inventoryid = inventoryid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getUnitsinstock() {
        return unitsinstock;
    }

    public void setUnitsinstock(Integer unitsinstock) {
        this.unitsinstock = unitsinstock;
    }

    public Integer getUnitsinreserved() {
        return unitsinreserved;
    }

    public void setUnitsinreserved(Integer unitsinreserved) {
        this.unitsinreserved = unitsinreserved;
    }

    public LocalDate getLastrestockdate() {
        return lastrestockdate;
    }

    public void setLastrestockdate(LocalDate lastrestockdate) {
        this.lastrestockdate = lastrestockdate;
    }

    public LocalDateTime getUpdateat() {
        return updateat;
    }

    public void setUpdateat(LocalDateTime updateat) {
        this.updateat = updateat;
    }

    //---------------
    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
