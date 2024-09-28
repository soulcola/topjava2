package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.topjava2.HasId;
import ru.javaops.topjava2.config.MoneyDeserializer;
import ru.javaops.topjava2.config.MoneySerializer;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "dish")
@Getter
@Setter
public class Dish extends NamedEntity implements HasId {

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default now()")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate createdAt;


    @Column(name = "price", nullable = false)
    @Positive
    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private long price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Integer id, String name, LocalDate createdAt, long price, Restaurant restaurant) {
        super(id, name);
        this.createdAt = createdAt;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Dish(Integer id, String name, LocalDate createdAt, long price) {
        this(id, name, createdAt, price, null);
    }

    public Dish(String name, LocalDate createdAt, long price) {
        this(null, name, createdAt, price);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", restaurant=" + restaurant +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dish dish = (Dish) o;
        return price == dish.price && Objects.equals(createdAt, dish.createdAt) && Objects.equals(restaurant, dish.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdAt, price, restaurant);
    }
}
