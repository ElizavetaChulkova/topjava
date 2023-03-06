package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({"postgres", "jdbc"})
public class JdbcMealServiceTest extends MealServiceTest {
}
