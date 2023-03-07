package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"hsqldb", "jdbc"})
public class JdbcUserServiceTest extends UserServiceTest {
}
