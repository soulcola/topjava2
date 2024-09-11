package ru.javaops.topjava2.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.service.DishService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Dish Controller")
public class AdminDishController {
    static final String REST_URL = "/api/admin/dishes";
    static final String REST_URL_RESTAURANT = AdminRestaurantController.REST_URL + "/{restaurantId}/dishes";

    private final DishRepository repository;
    private final DishService service;

    @Operation(summary = "Get all dishes by restaurant id")
    @GetMapping(REST_URL + "/all-by-restaurant")
    public List<Dish> getAllByRestaurantId(@RequestParam int restaurantId) {
        log.info("get all dishes for restaurant {}", restaurantId);
        return repository.getAllByRestaurantId(restaurantId);
    }

    @Operation(summary = "Get today dishes by restaurant id")
    @GetMapping(REST_URL + "/today-by-restaurant")
    public List<Dish> getTodayByRestaurantId(@RequestParam int restaurantId) {
        log.info("get today dishes for restaurant {}", restaurantId);
        return repository.getByDateAndRestaurantId(restaurantId, LocalDate.now());
    }


    @Operation(summary = "Get dish by id")
    @GetMapping(REST_URL + "/{id}")
    public Dish getById(@PathVariable int id) {
        log.info("get dish {}", id);
        return repository.getExisted(id);
    }

    @Operation(summary = "Create new dish")
    @CacheEvict(value = "restaurantCache", allEntries = true)
    @PostMapping(value = REST_URL_RESTAURANT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> create(@RequestBody @Valid Dish dish,
                                       @PathVariable int restaurantId) {
        log.info("Add dish {} to restaurant {}", dish, restaurantId);
        checkNew(dish);
        var created = service.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Delete dish by id")
    @CacheEvict(value = "restaurantCache", allEntries = true)
    @DeleteMapping(REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish {}", id);
        repository.deleteExisted(id);
    }

    @Operation(summary = "Update dish by id and restaurant id")
    @CacheEvict(value = "restaurantCache", allEntries = true)
    @PutMapping(value = REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Dish dish,
                       @RequestParam int restaurantId,
                       @PathVariable int id) {
        log.info("update {} with id {} for restaurant {}", dish, id, restaurantId);
        assureIdConsistent(dish, id);
        service.update(dish, restaurantId);
    }
}
