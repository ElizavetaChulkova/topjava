package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @Autowired
    public JspMealController(MealService mealService) {
        super(mealService);
    }

    @GetMapping
    public String getMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("meal", super.get(id));
        return "meals";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        super.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "/meals";
    }

    @GetMapping("/new")
    public String newMeal(Model model) {
        model.addAttribute("meal", new Meal());
        model.addAttribute("method", "POST");
        return "mealForm";
    }

    @GetMapping("/{id}/update")
    public String updateMeal(Model model, @PathVariable("id") int id) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping
    public String createOrUpdate(HttpServletRequest request) {
        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(getMealFromRequest(request), Integer.parseInt(request.getParameter("id")));
        } else {
            super.create(getMealFromRequest(request));
        }
        return "redirect:/meals";
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
