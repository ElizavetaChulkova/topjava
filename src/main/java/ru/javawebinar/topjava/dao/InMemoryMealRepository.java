package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.Collection;

public interface InMemoryMealRepository {
    Collection<MealTo> getAll();

    void save(Meal meal);

    void update(int id, Meal meal);

    MealTo get(int id);

    void delete(int id);
}
