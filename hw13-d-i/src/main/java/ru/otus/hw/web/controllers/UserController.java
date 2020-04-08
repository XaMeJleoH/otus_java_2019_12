package ru.otus.hw.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;

import java.util.List;


@Controller
public class UserController {

    private final DBServiceWebUser dbService;

    public UserController(DBServiceWebUser dbService) {
        this.dbService = dbService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        List<User> users = dbService.getAllUsers();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        dbService.saveUser(user);
        return new RedirectView("/user/list", true);
    }

}
