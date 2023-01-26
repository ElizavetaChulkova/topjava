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

public class UserMealsUtil extends TimeUtil{
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println();

        System.out.println(filteredByCyclesAndMaps(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List <UserMealWithExcess> mealsWithExcessBetweenStartAndEndTime = new ArrayList<>();
        int summaryOfUsersCaloriesPerDay = 0;
        boolean excess = false;

        LocalDate localDate = meals.get(0).getDateTime().toLocalDate();
        for(UserMeal m : meals){
            LocalDate mLocalDate = m.getDateTime().toLocalDate();
                if (localDate.equals(mLocalDate)){
                    summaryOfUsersCaloriesPerDay += m.getCalories();
                } else {
                    excess = caloriesPerDay >= summaryOfUsersCaloriesPerDay;
                    localDate = mLocalDate;
                    summaryOfUsersCaloriesPerDay = m.getCalories();
                }
                if (isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime)){
                    mealsWithExcessBetweenStartAndEndTime.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess));
                }
        }
        return mealsWithExcessBetweenStartAndEndTime;
    }

    public static List<UserMealWithExcess> filteredByCyclesAndMaps(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay){

        Map<LocalDate, Integer> usersCaloriesPerDate = new HashMap<>();

        for(UserMeal m : meals){
            if (usersCaloriesPerDate.containsKey(m.getDateTime().toLocalDate())){
                usersCaloriesPerDate.merge(m.getDateTime().toLocalDate(), m.getCalories(), (old, n) -> old + m.getCalories());
            } else {
                usersCaloriesPerDate.put(m.getDateTime().toLocalDate(), m.getCalories());
            }
        }

        List <UserMealWithExcess> mealsWithExcessBetweenStartAndEndTime = new ArrayList<>();

        for (UserMeal m : meals){
            boolean excess = caloriesPerDay < usersCaloriesPerDate.get(m.getDateTime().toLocalDate());
            if (isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcessBetweenStartAndEndTime.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess));
            }
        }
        return mealsWithExcessBetweenStartAndEndTime;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> resultList = meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), toFindExcess(meals, m.getDateTime().toLocalDate(), caloriesPerDay)))
                .collect(toList());

        return resultList;
    }
    public static boolean toFindExcess(List<UserMeal> meals, LocalDate ld, int caloriesPerDay){

        Map<LocalDate, Integer> usersCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        return caloriesPerDay < usersCaloriesPerDay.get(ld);
    }
}
