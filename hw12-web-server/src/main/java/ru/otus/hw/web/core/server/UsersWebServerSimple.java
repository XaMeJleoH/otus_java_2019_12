package ru.otus.hw.web.core.server;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.model.Address;
import ru.otus.core.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.web.core.helpers.FileSystemHelper;
import ru.otus.hw.web.core.services.TemplateProcessor;
import ru.otus.hw.web.core.servlet.CreateUserServlet;
import ru.otus.hw.web.core.servlet.UsersServlet;

@Slf4j
public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";

    private final DBServiceWebUser dbServiceWebUser;
    private final LoginService loginService;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerSimple(int port, DBServiceWebUser dbServiceWebUser, LoginService loginService,
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

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();
        initNewUsers(dbServiceWebUser);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
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

    private void initNewUsers(DBServiceWebUser dbServiceWebUser) {
        User sheldon = new User("Sheldon", 31, new Address("Mira, d.2, flat 255"));
        long sheldonId = dbServiceWebUser.saveUser(sheldon);
        log.warn("Sheldon was inserted, sheldonId={}", sheldonId);

        User penny = new User("Penny", 27, new Address("Mira, d.2, flat 256"));
        long pennyId = dbServiceWebUser.saveUser(penny);
        log.warn("Penny was inserted, him pennyId={}", pennyId);
    }

}
