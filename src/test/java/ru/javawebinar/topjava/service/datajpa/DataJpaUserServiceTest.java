package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"hsqldb", "datajpa"})
public class DataJpaUserServiceTest extends UserServiceTest {
}
