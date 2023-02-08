package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Collection<Meal> getAll();

    Meal create(Meal meal);

    Meal edit(Meal meal);

    Meal get(int id);

    void delete(int id);
}
