package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), "ADMIN_breakfast", 500), 2);
        save(new Meal(LocalDateTime.of(2023, Month.JANUARY, 2, 12, 0), "ADMIN_launch", 800), 2);
        save(new Meal(LocalDateTime.of(2023, Month.JANUARY, 30, 20, 0), "ADMIN_dinner", 2100), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, userMeal -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
        } else if (!meal.isNew()) {
            if (userMeals.get(meal.getId()) == null) return null;
            else userMeals.put(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? Collections.emptyList() :
                userMeals.values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDate(LocalDate startDate, LocalDate endDate, int userId) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(),
                        startDate,
                        endDate))
                .collect(Collectors.toList());
    }
}

