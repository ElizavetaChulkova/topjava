package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            jdbcTemplate.update("INSERT INTO meals (date_time, description, calories, user_id) VALUES (?, ?, ?, ?)",
                    meal.getDateTime(), meal.getDescription(), meal.getCalories(), userId);
        } else {
            jdbcTemplate.update("UPDATE meals SET date_time=?, description=?, calories=? WHERE id=? and user_id=?",
                    meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId(), userId);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? and user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE id=? and user_id=?",
                new BeanPropertyRowMapper<>(Meal.class), new Object[]{id, userId}).stream().findAny().orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?",
                new BeanPropertyRowMapper<>(Meal.class), userId)
                .stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE (date_time >= ? and date_time < ?) and user_id =?",
                new BeanPropertyRowMapper<>(Meal.class), startDateTime, endDateTime, userId)
                .stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }
}
