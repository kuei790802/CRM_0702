package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productimgs")
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgid;
    private String imgurl;

    public Long getImgid() {
        return imgid;
    }

    public void setImgid(Long imgid) {
        this.imgid = imgid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    //---------
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
