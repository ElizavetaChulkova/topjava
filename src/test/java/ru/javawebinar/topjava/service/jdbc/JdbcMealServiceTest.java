package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({"hsqldb", "jdbc"})
public class JdbcMealServiceTest extends MealServiceTest {
}
