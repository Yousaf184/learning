package com.ysf.spring6.rest.mvc.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @ManyToMany(mappedBy = "categories")
    private Set<Beer> beers = new HashSet<>();

    public void addBeer(Beer beer) {
        if (this.beers == null) {
            this.beers = new HashSet<>();
        }
        this.beers.add(beer);
    }

    public void removeBeer(Beer beer) {
        if (this.beers == null) {
            return;
        }
        this.beers.remove(beer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category otherCategory = (Category) o;
        return this.id != null && this.id.equals(otherCategory.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}