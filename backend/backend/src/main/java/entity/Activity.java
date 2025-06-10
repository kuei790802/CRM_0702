package entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activtyid")
    private Long id;

    @Column(name = "relatedtype")
    private String relatedType;

    @Column(name = "relatedid")
    private Long relatedId;

    @Column(name = "activtytype")
    private String type;

    @Column(name = "activtydate")
    private Date date;

    @Column(name = "activtynotes")
    private String notes;

    @Column(name = "salespersonname")
    private String sales;

    @Column(name = "activtystatus")
    private String status;

    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
