package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunityid")
    private Long id;

    @Column(name = "cutomerid")
    private Long customerId;

    @Column(name = "opportunitystage")
    private String stage;

    @Column(name = "opportunitystatus")
    private String status;

    @Column(name = "opportunityclosedate")
    private Date opportunityClosedate;

    @Column(name = "opportunityamount")
    private BigDecimal amount;

    @Column(name = "salespersonname")
    private String salesPersonName;

    @Column(name = "opportunitycreated")
    private LocalDateTime opportunityCreated;

    @Column(name = "opportunityupdated")
    private LocalDateTime opportunityUpdated;

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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOpportunityClosedate() {
        return opportunityClosedate;
    }

    public void setOpportunityClosedate(Date opportunityClosedate) {
        this.opportunityClosedate = opportunityClosedate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public LocalDateTime getOpportunityCreated() {
        return opportunityCreated;
    }

    public void setOpportunityCreated(LocalDateTime opportunityCreated) {
        this.opportunityCreated = opportunityCreated;
    }

    public LocalDateTime getOpportunityUpdated() {
        return opportunityUpdated;
    }

    public void setOpportunityUpdated(LocalDateTime opportunityUpdated) {
        this.opportunityUpdated = opportunityUpdated;
    }
}
