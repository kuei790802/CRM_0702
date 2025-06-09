package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerid")
    private Long id;

    @Column(name = "customername")
    private String name;

    @Column(name = "industry")
    private String industry;

    @Column(name = "customertype")
    private String type;

    @Column(name = "sourceid")
    private Long sourceid;

    @Column(name = "customerlevel")
    private String level;

    @Column(name = "customeraddress")
    private String address;

    @Column(name = "customertel")
    private String tel;

    @Column(name = "customeremail")
    private String email;

    @Column(name = "customercreated")
    private LocalDateTime customerCreated;

    @Column(name = "customerupdated")
    private LocalDateTime customerUpdated;

    // Getter and Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSourceid() {
        return sourceid;
    }

    public void setSourceid(Long sourceid) {
        this.sourceid = sourceid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCustomerCreated() {
        return customerCreated;
    }

    public void setCustomerCreated(LocalDateTime customerCreated) {
        this.customerCreated = customerCreated;
    }

    public LocalDateTime getCustomerUpdated() {
        return customerUpdated;
    }

    public void setCustomerUpdated(LocalDateTime customerUpdated) {
        this.customerUpdated = customerUpdated;
    }
}
