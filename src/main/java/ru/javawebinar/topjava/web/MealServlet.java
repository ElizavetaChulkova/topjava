package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealRepository;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository;

    public void init() {
        mealRepository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            log.info("Get the Meals table");
            request.setAttribute("mealList", MealsUtil.getMealsWithExceeded(mealRepository.getAll(), MealsUtil.CALORIES_PER_DATE));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
        switch (action) {
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                log.debug("Edit meal with id: {}", id);
                request.setAttribute("meal", MealsUtil.getMealToById(MealsUtil.getMealsWithExceeded(mealRepository.getAll(), MealsUtil.CALORIES_PER_DATE), id));
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                break;
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                log.info("Delete meal with id: {}", id);
                mealRepository.delete(id);
                //request.setAttribute("mealList", MealsUtil.getMealsWithExceeded(mealRepository.getAll(), MealsUtil.CALORIES_PER_DATE));
                response.sendRedirect("/topjava/meals");
                break;
            case "create":
                log.info(LocalDateTime.now() + " Create new meal");
                request.getRequestDispatcher("/createMeal.jsp").forward(request, response);
                break;
            default:
                log.info(LocalDateTime.now() + " Get the Meals table");
                request.setAttribute("mealList", MealsUtil.getMealsWithExceeded(mealRepository.getAll(), MealsUtil.CALORIES_PER_DATE));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String id = request.getParameter("id");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        if (id.isEmpty() || id.equals(null)) {
            mealRepository.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            mealRepository.edit(meal);
        }
        response.sendRedirect("/topjava/meals");
    }
}
