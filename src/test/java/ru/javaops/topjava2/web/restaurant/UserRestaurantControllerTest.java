package ru.javaops.topjava2.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.testdata.RestaurantTestData;
import ru.javaops.topjava2.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.TestUtil.userHttpBasic;
import static ru.javaops.topjava2.testdata.DishTestData.DISH_MATCHER;
import static ru.javaops.topjava2.testdata.DishTestData.todayDishes;
import static ru.javaops.topjava2.testdata.RestaurantTestData.RESTAURANT_MATCHER;
import static ru.javaops.topjava2.testdata.RestaurantTestData.RESTAURANT_MATCHER_WITH_DISHES;
import static ru.javaops.topjava2.testdata.UserTestData.user;
import static ru.javaops.topjava2.web.restaurant.UserRestaurantController.REST_URL;

class UserRestaurantControllerTest extends AbstractControllerTest {

    @Test
    void getToday() throws Exception {
        var action = perform(MockMvcRequestBuilders.get(REST_URL + "/today-with-dishes")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        var restaurants = RESTAURANT_MATCHER_WITH_DISHES.readListFromJson(action);

        var dishes = restaurants.stream()
                .flatMap(restaurant -> restaurant.getDishes().stream())
                .toList();
        RESTAURANT_MATCHER.assertMatch(restaurants, RestaurantTestData.restaurantsWithTodayDishes);
        DISH_MATCHER.assertMatch(dishes, todayDishes);
    }
}