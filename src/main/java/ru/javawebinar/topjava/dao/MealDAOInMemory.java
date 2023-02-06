package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.InMemoryMealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAOInMemory implements InMemoryMealRepository {
    public static AtomicInteger mealCount = new AtomicInteger(0);

    List<Meal> meals = new ArrayList<>();

    {
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(mealCount.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    }

    @Override
    public List<MealTo> getAll() {
        return MealsUtil.getMealsWithExceeded(meals);
    }

    @Override
    public void save(Meal meal) {
        meal.setId(mealCount.incrementAndGet());
        meals.add(meal);
    }

    @Override
    public void update(int id, Meal updatedMeal) {
        delete(id);
        meals.add(new Meal(id, updatedMeal.getDateTime(), updatedMeal.getDescription(), updatedMeal.getCalories()));
    }

    @Override
    public MealTo get(int id) {
        return MealsUtil.getMealsWithExceeded(meals).stream().filter(meal -> meal.getId() == id).findAny().orElse(null);
    }

    @Override
    public void delete(int id) {
        meals.removeIf(meal -> meal.getId() == id);
    }

}
