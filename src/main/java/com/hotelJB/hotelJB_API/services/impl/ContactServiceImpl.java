package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ContactDTO;
import com.hotelJB.hotelJB_API.models.entities.Contact;
import com.hotelJB.hotelJB_API.models.responses.ContactResponse;
import com.hotelJB.hotelJB_API.repositories.ContactRepository;
import com.hotelJB.hotelJB_API.services.ContactService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContactResponse getContact() {
        Contact contact = repository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró información de contacto"));
        return mapToResponse(contact);
    }

    @Override
    public ContactResponse updateContact(ContactDTO dto) {
        Contact contact = repository.findAll().stream().findFirst().orElse(null);

        if (contact == null) {
            contact = new Contact(); // Crear nuevo contacto si no existe
        }

        contact.setTelephone(dto.getTelephone());
        contact.setTelephone2(dto.getTelephone2());
        contact.setEmail(dto.getEmail());
        contact.setAddress(dto.getAddress());
        contact.setAddressUrl(dto.getAddressUrl());
        contact.setFacebookUsername(dto.getFacebookUsername());
        contact.setFacebookUrl(dto.getFacebookUrl());
        contact.setInstagramUsername(dto.getInstagramUsername());
        contact.setTiktok(dto.getTiktok());

        return mapToResponse(repository.save(contact));
    }


    private ContactResponse mapToResponse(Contact entity) {
        ContactResponse res = new ContactResponse();
        BeanUtils.copyProperties(entity, res);
        return res;
    }
}

