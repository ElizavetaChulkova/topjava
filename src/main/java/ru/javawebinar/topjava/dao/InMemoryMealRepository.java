package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private AtomicInteger mealCounter = new AtomicInteger(0);

    private ConcurrentMap<Integer, Meal> repository = new ConcurrentHashMap<>();

    List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    {
        for (Meal meal : meals){
            create(meal);
        }
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(mealCounter.incrementAndGet());
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal edit(Meal updatedMeal) {
        if (isExisted(updatedMeal.getId())) {
            repository.put(updatedMeal.getId(), new Meal(updatedMeal.getId(), updatedMeal.getDateTime(),
                    updatedMeal.getDescription(), updatedMeal.getCalories()));
            return updatedMeal;
        } else return null;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    public boolean isExisted(int id){
        return repository.containsKey(id);
    }
}
