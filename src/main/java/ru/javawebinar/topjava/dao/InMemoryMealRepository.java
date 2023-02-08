package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    private AtomicInteger mealCounter = new AtomicInteger(0);

    public static int caloriesPerDay = 2000;

    private ConcurrentMap<Integer, Meal> repository = new ConcurrentHashMap<>();

    {
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(mealCounter.incrementAndGet());
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal edit(Meal updatedMeal) {
        repository.put(updatedMeal.getId(), new Meal(updatedMeal.getDateTime(),
                updatedMeal.getDescription(), updatedMeal.getCalories()));
        return updatedMeal;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }
}
