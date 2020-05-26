package ru.otus.hw.web.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.db.model.User;
import java.util.List;

@Slf4j
@Controller
public class UserController {

    private static final String INDEX_PAGE_TEMPLATE = "index";
    private static final String CREATE_USER_PAGE_TEMPLATE = "create_user";
    private static final String USERS_PAGE_TEMPLATE = "users";
    @GetMapping("/")
    public String  startPageView() {
        return INDEX_PAGE_TEMPLATE;
    }

    @GetMapping({"/users"})
    public String userListView(Model model) {
        //List<User> users = dbService.getAllUsers();
        model.addAttribute("users", new User());
        return USERS_PAGE_TEMPLATE;
    }

    @GetMapping("create_user")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return CREATE_USER_PAGE_TEMPLATE;
    }

}
