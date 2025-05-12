package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ContactDTO;
import com.hotelJB.hotelJB_API.models.entities.Contact;
import com.hotelJB.hotelJB_API.repositories.ContactRepository;
import com.hotelJB.hotelJB_API.services.ContactService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(ContactDTO data) throws Exception {
        try {
            Optional<Contact> optionalContact = contactRepository.findFirst();

            Contact contact;
            if (optionalContact.isPresent()) {
                // Si ya existe un contacto, actualizar los datos
                contact = optionalContact.get();
            } else {
                // Si no existe, crear uno nuevo
                contact = new Contact();
            }

            contact.setTelephone(data.getTelephone());
            contact.setTelephone2(data.getTelephone2());
            contact.setAddress(data.getAddress());
            contact.setAddressUrl(data.getAddressUrl());
            contact.setEmail(data.getEmail());
            contact.setInstagram(data.getInstagramUsername());
            contact.setFacebookUsername(data.getFacebookUsername());
            contact.setFacebookUrl(data.getFacebookUrl());
            contact.setTiktok(data.getTiktok());

            contactRepository.save(contact);
        } catch (Exception e) {
            throw new Exception("Error saving or updating contact", e);
        }
    }


    @Override
    public void update(ContactDTO data, int contactId) throws Exception {
        try{
            Contact contact = contactRepository.findById(contactId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category"));

            contact.setTelephone(data.getTelephone());
            contact.setTelephone2(data.getTelephone2());
            contact.setAddress(data.getAddress());
            contact.setAddressUrl(data.getAddressUrl());
            contact.setEmail(data.getEmail());
            contact.setInstagram(data.getInstagramUsername());
            contact.setFacebookUsername(data.getFacebookUsername());
            contact.setFacebookUrl(data.getFacebookUrl());
            contact.setTiktok(data.getTiktok());

            contactRepository.save(contact);
        }catch (Exception e){
            throw new Exception("Error update contact");
        }
    }

    @Override
    public void delete(int contactId) throws Exception {
        try{
            Contact contact = contactRepository.findById(contactId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Contact"));

            contactRepository.delete(contact);
        }catch (Exception e){
            throw new Exception("Error delete contact");
        }
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(int contactId) {
        return contactRepository.findById(contactId);
    }
}
