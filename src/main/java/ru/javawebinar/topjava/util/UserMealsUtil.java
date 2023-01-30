package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
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

        System.out.println();

        System.out.println(filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycle
            (List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> filteredMealsWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDates = new HashMap<>();
        if (meals.isEmpty()) return null;
        filteredByRecursion(meals, caloriesPerDates, 0, startTime, endTime, caloriesPerDay, filteredMealsWithExcess);
        return filteredMealsWithExcess;
    }

    public static void filteredByRecursion(List<UserMeal> meals, Map<LocalDate, Integer> caloriesPerDates,
                                           int index, LocalTime startTime, LocalTime endTime, int caloriesPerDay,
                                           List<UserMealWithExcess> filteredMealsWithExcess) {
        if (index == meals.size()) return;
        int finalIndex = index;
        caloriesPerDates.merge(meals.get(index).getDateTime().toLocalDate(),
                meals.get(index).getCalories(), (previousValue, newValue) -> previousValue + meals.get(finalIndex).getCalories());
        filteredByRecursion(meals, caloriesPerDates, ++index, startTime, endTime, caloriesPerDay, filteredMealsWithExcess);
        if (isBetweenHalfOpen(meals.get(index - 1).getDateTime().toLocalTime(), startTime, endTime)) {
            filteredMealsWithExcess.add(new UserMealWithExcess(meals.get(index - 1).getDateTime(), meals.get(index - 1).getDescription(),
                    meals.get(index - 1).getCalories(),
                    caloriesPerDates.get(meals.get(index - 1).getDateTime().toLocalDate()) > caloriesPerDay));
        }
    }

    public static List<UserMealWithExcess> filteredByCycles
            (List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> usersCaloriesPerDates = new HashMap<>();
        for (UserMeal m : meals) {
            usersCaloriesPerDates.merge(m.getDateTime().toLocalDate(), m.getCalories(),
                    (previousValue, newValue) -> previousValue + m.getCalories());
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

    public static List<UserMealWithExcess> filteredByStreams
            (List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> usersCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(),
                        caloriesPerDay < usersCaloriesPerDay.get(m.getDateTime().toLocalDate())))
                .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(GroupingCaloriesPerDates.distributeCaloriesPerDates(meals, caloriesPerDay, startTime, endTime));
    }
}

class GroupingCaloriesPerDates implements Collector<UserMeal, Map<LocalDate, Integer>, List<UserMealWithExcess>> {
    private static int caloriesPerDay;
    private static List<UserMeal> meals;
    private LocalTime startTime;
    private LocalTime endTime;

    public GroupingCaloriesPerDates(List<UserMeal> meals,
                                    int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.meals = meals;
        this.caloriesPerDay = caloriesPerDay;
    }

    public static GroupingCaloriesPerDates distributeCaloriesPerDates(List<UserMeal> meals,
                                                                      int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return new GroupingCaloriesPerDates(meals,
                caloriesPerDay, startTime, endTime);
    }

    @Override
    public Supplier<Map<LocalDate, Integer>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<LocalDate, Integer>, UserMeal> accumulator() {
        return (map, meal) -> map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(),
                (previousValue, newValue) -> previousValue + meal.getCalories());
    }

    @Override
    public BinaryOperator<Map<LocalDate, Integer>> combiner() {
        return (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        };
    }

    @Override
    public Function<Map<LocalDate, Integer>, List<UserMealWithExcess>> finisher() {
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        return map -> {
            for (UserMeal meal : meals) {
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    userMealWithExcess.add(new UserMealWithExcess(meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
                }
            }
            return userMealWithExcess;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.UNORDERED);
    }
}
