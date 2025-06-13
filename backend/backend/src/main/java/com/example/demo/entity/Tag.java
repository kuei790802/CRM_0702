package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagid")
    private Long id;

    @Column(name = "tagname")
    private String name;

    // -----
    @ManyToMany(mappedBy = "tags")
    private Set<BCustomer> BCustomers = new HashSet<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

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

    public Set<BCustomer> getCustomers() {
        return BCustomers;
    }

    public void setCustomers(Set<BCustomer> BCustomers) {
        this.BCustomers = BCustomers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id != null && id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
