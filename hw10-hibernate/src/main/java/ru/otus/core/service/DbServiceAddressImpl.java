package ru.otus.core.service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.dao.AddressDao;
import ru.otus.core.model.Address;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

@Slf4j
public class DbServiceAddressImpl implements DBServiceAddress {

    private final AddressDao addressDao;

    public DbServiceAddressImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public long saveAddress(Address address) {
        try (SessionManager sessionManager = addressDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long addressId = addressDao.saveAddress(address);
                sessionManager.commitSession();

                log.info("created address: {}", addressId);
                return addressId;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<Address> getAddress(long id) {
        try (SessionManager sessionManager = addressDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Address> addressOptional = addressDao.findById(id);

                log.info("address: {}", addressOptional.orElse(null));
                return addressOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public void updateAddress(Address address) {
        try (SessionManager sessionManager = addressDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                addressDao.updateAddress(address);
                sessionManager.commitSession();
                log.info("updated address: {}", address.getId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

}
