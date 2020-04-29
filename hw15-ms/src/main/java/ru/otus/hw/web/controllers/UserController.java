package ru.otus.hw.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.front.FrontendService;

import java.util.List;

@Slf4j
@Controller
public class UserController {

    private static final String INDEX_PAGE_TEMPLATE = "index";
    private static final String CREATE_USER_PAGE_TEMPLATE = "create_user";
    private static final String USERS_PAGE_TEMPLATE = "users";
    private final FrontendService frontendService;

    public UserController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @GetMapping("/")
    public String  startPageView(Model model) {
        return INDEX_PAGE_TEMPLATE;
    }

    @GetMapping({"/users"})
    public String userListView(Model model) {
        frontendService.getUserData(1, data -> log.info("got data:{}", data));
        //List<User> users =
        //model.addAttribute("users", users);
        return USERS_PAGE_TEMPLATE;
    }

    @GetMapping("create_user")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return CREATE_USER_PAGE_TEMPLATE;
    }

    @PostMapping("create_user")
    public RedirectView userSave(@ModelAttribute User user) {
        //dbService.saveUser(user);
        return new RedirectView(USERS_PAGE_TEMPLATE, true);
    }

}
