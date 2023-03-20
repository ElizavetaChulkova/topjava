package ru.javawebinar.topjava.web.meal;

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
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService mealService) {
        super(mealService);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable("id") int id) {
        super.delete(id);
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
    public String addNew(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 100));
        model.addAttribute("method", "POST");
        return "mealForm";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping
    public String createOrUpdate(HttpServletRequest request) {
        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(getFromRequest(request), Integer.parseInt(request.getParameter("id")));
        } else {
            super.create(getFromRequest(request));
        }
        return "redirect:/meals";
    }

    private Meal getFromRequest(HttpServletRequest request) {
        return new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
