package ru.otus.hw.web.core.servlet;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.web.core.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CreateUserServlet extends HttpServlet {
    private static final String CREATE_USER_PAGE_TEMPLATE = "create_user.html";
    private static final String USERS_PAGE_TEMPLATE = "users";

    private final DBServiceWebUser dbServiceWebUser;
    private final TemplateProcessor templateProcessor;

    public CreateUserServlet(TemplateProcessor templateProcessor, DBServiceWebUser dbServiceWebUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CREATE_USER_PAGE_TEMPLATE, new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User newUser = new User();
        newUser.setName(req.getParameter("name"));
        newUser.setAge(Integer.parseInt(req.getParameter("age")));
        dbServiceWebUser.saveUser(newUser);

        resp.sendRedirect(USERS_PAGE_TEMPLATE);
    }


}
