package ru.otus.hw.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
