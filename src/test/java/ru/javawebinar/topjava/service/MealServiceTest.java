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

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
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
        Meal actual = mealService.get(meal1.getId(), USER_ID);
        assertEquals(actual, meal1);
    }

    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    public void getWrongOwner() {
        assertThrows(NotFoundException.class, () -> mealService.get(meal10.getId(), USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(meal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(meal1.getId(), USER_ID));
    }

    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    public void deleteWrongOwner() {
        assertThrows(NotFoundException.class, () -> mealService.delete(meal10.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(mealService.getBetweenInclusive(meal1.getDate(), meal3.getDate(), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        assertMatch(mealService.getAll(USER_ID),
                testMeals.stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList()));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updated.getId(), USER_ID), getUpdated());
    }

    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.update(meal10, USER_ID));
    }

    public void updateWrongOwner() {
        assertThrows(NotFoundException.class, () -> mealService.update(meal10, USER_ID));
    }

    @Test
    public void create() {
        Meal created = getNew();
        mealService.create(created, USER_ID);
        int newId = created.getId();
        assertMatch(mealService.get(newId, USER_ID), created);
    }

    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, meal1.getDateTime(), "duplicate date time", 500), USER_ID));
    }
}