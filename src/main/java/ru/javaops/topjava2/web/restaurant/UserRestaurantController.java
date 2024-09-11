package ru.javaops.topjava2.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant Controller")
public class UserRestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;

    @Operation(summary = "Get all restaurants with today dishes")
    @Cacheable("restaurantCache")
    @GetMapping(REST_URL + "/today-with-dishes")
    public List<Restaurant> getToday() {
        log.info("get today's restaurants");
        return repository.getAllByDateWithDishes(LocalDate.now());
    }
}
