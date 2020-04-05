package ru.otus.hw;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.web.core.helpers.FileSystemHelper;
import ru.otus.hw.web.core.server.UsersWebServer;
import ru.otus.hw.web.core.server.UsersWebServerImpl;
import ru.otus.hw.web.core.services.TemplateProcessor;

import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static ru.otus.hw.utils.HttpUrlConnectionHelper.*;

class WebServerTest {

    private static final int WEB_SERVER_PORT = 8181;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    private static final String API_USER_URL = "api/user";
    private static final String LIST_USERS_URL = "users";
    private static final String CREATE_USER_URL = "create_user";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    private static UsersWebServer webServer;

    @BeforeEach
    void setUp() throws Exception {
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
        DBServiceWebUser dbServiceWebUser = mock(DBServiceWebUser.class);

        webServer = new UsersWebServerImpl(WEB_SERVER_PORT, dbServiceWebUser, loginService, templateProcessor);
        webServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать 404 при неизвестной странице")
    @Test
    void openUnavailableAddress() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND);
    }

    @DisplayName("возвращать 401 при попытке входа неавторизованным на страницу списка пользователей")
    @Test
    void openListAddressWithoutAuthorization() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, LIST_USERS_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @DisplayName("возвращать 401 при попытке входа неавторизованным на страницу списка пользователей")
    @Test
    void openCreateAddressWithoutAuthorization() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, CREATE_USER_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @DisplayName("возвращать 200 при попытке входа на главную страницу")
    @Test
    void openHomePage() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, "index.html", null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @DisplayName("возвращать 302 при попытке входа по адресу хоста]")
    @Test
    void openEmptyAddressHomePage() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, "", null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }


    @DisplayName("возвращать 200 при попытке входа Авторизованным на страницу списка пользователей")
    @Test
    void openListAddressWithAuthorization() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, LIST_USERS_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Authorization", "Basic " + encodedLoginAndPassword(LOGIN, PASSWORD));
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @DisplayName("возвращать 200 при попытке входа Авторизованным на страницу списка пользователей")
    @Test
    void openCreateAddressWithAuthorization() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, CREATE_USER_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Authorization", "Basic " + encodedLoginAndPassword(LOGIN, PASSWORD));
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
    }

}