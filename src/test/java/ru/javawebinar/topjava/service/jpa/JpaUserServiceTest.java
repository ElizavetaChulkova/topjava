package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles({"hsqldb", "jpa"})
public class JpaUserServiceTest extends UserServiceTest {
}
