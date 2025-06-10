package entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "quotations")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotationid")
    private Long id;

    @Column(name = "opportunityid")
    private Long opportunityid ;

    @Column(name = "quotationdate")
    private Date date;

    @Column(name = "quotationvalid")
    private Date valid;

    @Column(name = "quotationtotal")
    private Double total;

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(Long opportunityid) {
        this.opportunityid = opportunityid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getValid() {
        return valid;
    }

    public void setValid(Date valid) {
        this.valid = valid;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
