package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil extends TimeUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Ужин", 2100)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println();

        System.out.println(filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }

    public static List<UserMealWithExcess> filteredByCycle(List<UserMeal> meals, LocalTime startTime,
                                                           LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> filteredMealsWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDates = new HashMap<>();
        if (meals.isEmpty()) return null;
        filteredByRecursion(meals, caloriesPerDates, 0, startTime, endTime, caloriesPerDay, filteredMealsWithExcess);
        return filteredMealsWithExcess;
    }

    private static void filteredByRecursion(List<UserMeal> meals, Map<LocalDate, Integer> caloriesPerDates,
                                            int index, LocalTime startTime, LocalTime endTime, int caloriesPerDay,
                                            List<UserMealWithExcess> filteredMealsWithExcess) {
        if (index == meals.size()) return;
        UserMeal mealGetIndex = meals.get(index);
        UserMeal mealGetIndexMinus1 = meals.get(index - 1);
        caloriesPerDates.merge(mealGetIndex.getDateTime().toLocalDate(),
                mealGetIndex.getCalories(), Integer::sum);
        filteredByRecursion(meals, caloriesPerDates, ++index, startTime, endTime, caloriesPerDay, filteredMealsWithExcess);
        if (isBetweenHalfOpen(mealGetIndexMinus1.getDateTime().toLocalTime(), startTime, endTime)) {
            filteredMealsWithExcess.add(new UserMealWithExcess(mealGetIndexMinus1.getDateTime(), mealGetIndexMinus1.getDescription(),
                    mealGetIndexMinus1.getCalories(),
                    caloriesPerDates.get(mealGetIndexMinus1.getDateTime().toLocalDate()) > caloriesPerDay));
        }
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> usersCaloriesPerDates = new HashMap<>();
        for (UserMeal m : meals) {
            usersCaloriesPerDates.merge(m.getDateTime().toLocalDate(), m.getCalories(),
                    Integer::sum);
        }
        List<UserMealWithExcess> filteredMealsWithExcess = new ArrayList<>();
        for (UserMeal m : meals) {
            boolean excess = caloriesPerDay < usersCaloriesPerDates.get(m.getDateTime().toLocalDate());
            if (isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMealsWithExcess.add(new UserMealWithExcess(m.getDateTime(),
                        m.getDescription(), m.getCalories(), excess));
            }
        }
        return filteredMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> usersCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(),
                        caloriesPerDay < usersCaloriesPerDay.get(m.getDateTime().toLocalDate())))
                .collect(toList());
    }
}
