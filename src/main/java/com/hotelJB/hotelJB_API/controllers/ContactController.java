package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ContactDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.responses.ContactResponse;
import com.hotelJB.hotelJB_API.services.ContactService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<ContactResponse> getContact() {
        return ResponseEntity.ok(contactService.getContact());
    }

    @PutMapping
    public ResponseEntity<ContactResponse> updateContact(@RequestBody ContactDTO dto) {
        return ResponseEntity.ok(contactService.updateContact(dto));
    }
}

