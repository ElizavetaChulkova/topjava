package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(entityManager.getReference(User.class, userId));
        if (meal.isNew()) {
            entityManager.persist(meal);
            return meal;
        } else if (get(meal.id(), userId) == null) {
            return null;
        }
        return entityManager.merge(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return entityManager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
//        return entityManager.createNamedQuery(Meal.GET, Meal.class)
//                .setParameter("id", id)
//                .setParameter("userId", userId)
//                .getSingleResult();
        Meal meal = entityManager.find(Meal.class, id);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter(1, startDateTime)
                .setParameter(2, endDateTime)
                .setParameter("userId", userId)
                .getResultList();
    }
}