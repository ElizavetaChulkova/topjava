package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAOInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDAOInMemory mealDAOInMemory;

    public void init() {
        mealDAOInMemory = new MealDAOInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            log.info("getAll");
            request.setAttribute("mealList", mealDAOInMemory.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("showAll")) {
            request.setAttribute("mealList", mealDAOInMemory.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("update")) {
            String id = request.getParameter("id");
            request.setAttribute("meal", mealDAOInMemory.get(Integer.parseInt(id)));
            request.getRequestDispatcher("/create-update.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            mealDAOInMemory.delete(id);
            request.setAttribute("mealList", mealDAOInMemory.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/create-update.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String id = request.getParameter("id");
        if (id.isEmpty()) {
            mealDAOInMemory.save(new Meal(TimeUtil.formatLocalDateTime(request.getParameter("dateTime")),
                    request.getParameter("description"), Integer.parseInt(request.getParameter("calories"))));
        } else {
            mealDAOInMemory.update(Integer.parseInt(id), new Meal(TimeUtil.formatLocalDateTime(request.getParameter("dateTime")),
                    request.getParameter("description"), Integer.parseInt(request.getParameter("calories"))));
        }
        request.setAttribute("mealList", mealDAOInMemory.getAll());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
