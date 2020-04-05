package ru.otus.hw.web.core.server;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.web.core.helpers.FileSystemHelper;
import ru.otus.hw.web.core.services.TemplateProcessor;
import ru.otus.hw.web.core.servlet.CreateUserServlet;
import ru.otus.hw.web.core.servlet.UsersServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class UsersWebServerImpl implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";

    private final DBServiceWebUser dbServiceWebUser;
    private final LoginService loginService;
    private final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerImpl(int port, DBServiceWebUser dbServiceWebUser, LoginService loginService,
                              TemplateProcessor templateProcessor) {
        this.dbServiceWebUser = dbServiceWebUser;
        this.loginService = loginService;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/create_user"));

        server.setHandler(handlers);
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_ADMIN});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));

        return security;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, dbServiceWebUser)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new CreateUserServlet(templateProcessor, dbServiceWebUser)), "/create_user");
        return servletContextHandler;
    }
}
