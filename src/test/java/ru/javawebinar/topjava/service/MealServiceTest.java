package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal actual = mealService.get(USER_MEAL_ID, USER_ID);
        assertMatch(actual, userMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND_MEAL, USER_ID));
    }

    @Test
    public void getWrongOwner() {
        assertThrows(NotFoundException.class, () -> mealService.get(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND_MEAL, USER_ID));
    }

    @Test
    public void deleteWrongOwner() {
        assertThrows(NotFoundException.class, () -> mealService.delete(adminMeal2.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(mealService.getBetweenInclusive(userMeal1.getDate(), userMeal3.getDate(), USER_ID),
                userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getAll() {
        assertMatch(mealService.getAll(USER_ID), userMeals);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updated.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateWrongOwner() {
        assertThrows(Exception.class, () -> mealService.update(userMeal1, ADMIN_ID));
    }

//    @Test
//    public void updateWrongOwner() {
//        Meal meal = new Meal(userMeal1.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 15), "updated by admin", 123);
//        assertThrows(NotFoundException.class, () -> mealService.update(meal, ADMIN_ID));
//        assertMatch(mealService.get(userMeal1.getId(), USER_ID), userMeal1);
//    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, userMeal1.getDateTime(), "duplicate date time", 500), USER_ID));
    }

    @Test
    public void filterWithNullParameters() {
        assertMatch(mealService.getBetweenInclusive(null, null, USER_ID),
                userMeals);
    }
}