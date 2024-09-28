package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<Dish> findByIdAndRestaurantID(@Param("id") int id,
                                           @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId")
    List<Dish> getAllByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.createdAt=:createdAt")
    List<Dish> getByDateAndRestaurantId(@Param("restaurantId") int restaurantId,
                                        @Param("createdAt") LocalDate createdAt);

    default Dish getByIdAndRestaurantId(int id, int restaurantId) {
        return findByIdAndRestaurantID(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("Dish with id " + id +
                        " and restaurant id " + restaurantId + " not found"));
    }
}
