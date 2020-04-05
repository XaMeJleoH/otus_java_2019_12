package ru.otus.hw.web.core.server;

import lombok.extern.slf4j.Slf4j;
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

    private final DBServiceWebUser dbServiceWebUser;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerSimple(int port, DBServiceWebUser dbServiceWebUser, TemplateProcessor templateProcessor) {
        this.dbServiceWebUser = dbServiceWebUser;
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
        User sheldon = getUser();
        long id = dbServiceWebUser.saveUser(sheldon);
        log.warn("Sheldon was inserted, him id={}", id);
    }

    private User getUser() {
        User sheldon = new User("Sheldon", 31, new Address("Mira"));

 /*       List<Phone> phones = new ArrayList<>();
        Phone phoneHome = new Phone();
        phoneHome.setUser(sheldon);
        phoneHome.setNumber("811");
        phones.add(phoneHome);

        Phone phoneWork = new Phone();
        phoneWork.setUser(sheldon);
        phoneWork.setNumber("4833 349934");
        phones.add(phoneWork);

        Phone phonePenny = new Phone();
        phonePenny.setUser(sheldon);
        phonePenny.setNumber("993333 33");
        phones.add(phonePenny);

        sheldon.setPhones(phones);*/
        return sheldon;
    }
}
