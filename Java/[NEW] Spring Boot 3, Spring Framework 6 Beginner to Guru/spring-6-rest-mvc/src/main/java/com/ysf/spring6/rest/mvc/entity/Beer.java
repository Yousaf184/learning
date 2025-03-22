package com.ysf.spring6.rest.mvc.entity;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.converter.BeerStyleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "beer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Version
    @Column(name = "opt_lock")
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 255)
    @Column(name = "beer_name")
    private String beerName;

    @NotNull
    @Column(name = "beer_style")
    @Convert(converter = BeerStyleConverter.class)
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    @Size(max = 255)
    @Column(name = "upc")
    private String upc;

    @NotNull
    @Max(1_000_000_000) // 1 billion
    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "beer_category",
            joinColumns = @JoinColumn(name = "beer_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    public void addCategory(Category category) {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        this.categories.add(category);
        category.addBeer(this);
    }

    public void removeCategory(Category category) {
        if (this.categories == null) {
            return;
        }
        this.categories.remove(category);
        category.removeBeer(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Beer otherBeer = (Beer) o;
        return this.id != null && this.id.equals(otherBeer.getId());
    }

    @Override
    public final int hashCode() {
        return this.getClass().hashCode();
    }
}