package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int authUserId = 1;

    public static int authUserId() {
        return authUserId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setAuthUserId(String userId){
        SecurityUtil.authUserId = Integer.parseInt(userId);
    }
}