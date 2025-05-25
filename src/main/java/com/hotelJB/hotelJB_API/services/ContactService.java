package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.ContactDTO;
import com.hotelJB.hotelJB_API.models.entities.Contact;
import com.hotelJB.hotelJB_API.models.responses.ContactResponse;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    ContactResponse getContact();
    ContactResponse updateContact(ContactDTO dto);
}

