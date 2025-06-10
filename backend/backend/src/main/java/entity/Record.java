package entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordid")
    private Long id;

    @Column(name = "customerid")
    private Long customerId;

    @Column(name = "opportunityid")
    private Long opportunityId;

    @Column(name = "salesdate")
    private Date date;

    @Column(name = "totalamount")
    private Double amount;

    @Column(name = "salespersonname")
    private Double salesPersonName;

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Long opportunityId) {
        this.opportunityId = opportunityId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(Double salesPersonName) {
        this.salesPersonName = salesPersonName;
    }
}
