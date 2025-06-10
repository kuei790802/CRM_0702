package entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventid")
    private Long id;

    @Column(name = "eventtitle")
    private String tilte;

    @Column(name = "eventstart")
    private Date start;

    @Column(name = "eventend")
    private Date end;

    @Column(name = "salespersonname")
    private String salesPersonName;

    @Column(name = "customerid")
    private Long customerid;

    @Column(name = "opportunityid")
    private Long opportunityid ;

    @Column(name = "eventnotes")
    private String notes;

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String sales) {
        this.salesPersonName = sales;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(Long opportunityid) {
        this.opportunityid = opportunityid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
