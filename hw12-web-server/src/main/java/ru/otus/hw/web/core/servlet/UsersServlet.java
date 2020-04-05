package ru.otus.hw.web.core.servlet;

import ru.otus.core.service.DBServiceUser;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.web.core.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "user";

    private final DBServiceWebUser dbServiceWebUser;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceWebUser dbServiceWebUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        // TODO: 04.04.2020
        dbServiceWebUser.getAllUsers().forEach(user -> paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, user));
        //userDao.findRandomUser().ifPresent(randomUser -> paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, randomUser));

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
