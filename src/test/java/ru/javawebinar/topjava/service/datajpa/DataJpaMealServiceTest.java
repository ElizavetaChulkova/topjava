package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({"hsqldb", "datajpa"})
public class DataJpaMealServiceTest extends MealServiceTest {
}
