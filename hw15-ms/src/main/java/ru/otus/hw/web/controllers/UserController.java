package ru.otus.hw.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
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
    private final DBServiceWebUser dbService;
    private final FrontendService frontendService;
    private final ObjectMapper objectMapper;

    public UserController(DBServiceWebUser dbService, FrontendService frontendService, ObjectMapper objectMapper) {
        this.dbService = dbService;
        this.frontendService = frontendService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String  startPageView() {
        return INDEX_PAGE_TEMPLATE;
    }

    @GetMapping({"/users"})
    public String userListView(Model model) {
        List<User> users = dbService.getAllUsers();
        model.addAttribute("users", users);
        return USERS_PAGE_TEMPLATE;
    }

    @GetMapping("create_user")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return CREATE_USER_PAGE_TEMPLATE;
    }

    @SneakyThrows
    @PostMapping("create_user")
    public RedirectView userSave(@ModelAttribute User user) {
        frontendService.saveUserData(objectMapper.writeValueAsString(user), userData -> log.info("got message: {}", userData));
        return new RedirectView(USERS_PAGE_TEMPLATE, true);
    }

}
