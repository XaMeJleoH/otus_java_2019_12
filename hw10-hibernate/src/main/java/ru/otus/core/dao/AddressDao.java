package ru.otus.core.dao;

import ru.otus.core.model.Address;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AddressDao {
  Optional<Address> findById(long id);

  long saveAddress(Address address);

  void updateAddress(Address address);

  SessionManager getSessionManager();
}
