package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.ContactDTO;
import com.hotelJB.hotelJB_API.models.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    void save(ContactDTO data) throws Exception;
    void update(ContactDTO data, int contactId) throws Exception;
    void delete(int contactId) throws Exception;
    List<Contact> getAll();
    Optional<Contact> findById(int contactId);
}
