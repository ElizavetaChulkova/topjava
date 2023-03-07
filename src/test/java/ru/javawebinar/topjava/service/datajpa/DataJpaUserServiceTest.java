package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles({"hsqldb", "datajpa"})
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getUserWithAllMeals() {
        User actual = service.getUserWithAllMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), MealTestData.meals);
    }
}
