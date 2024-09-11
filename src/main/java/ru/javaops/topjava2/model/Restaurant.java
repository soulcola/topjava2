package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.javaops.topjava2.HasId;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedEntity implements HasId {

    @OneToMany(mappedBy = "restaurant")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public List<Dish> dishes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.dishes);
    }

    public Restaurant(Integer id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
        this.dishes = Collections.emptyList();
    }

    public Restaurant(String name) {
        this(null, name);
        this.dishes = Collections.emptyList();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "dishes=" + dishes +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
