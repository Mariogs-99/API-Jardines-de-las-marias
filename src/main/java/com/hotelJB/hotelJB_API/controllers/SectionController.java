package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.SectionsDTO;
import com.hotelJB.hotelJB_API.services.SectionsService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/section")
public class SectionController {
    @Autowired
    private SectionsService sectionService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody SectionsDTO data, BindingResult validations) throws Exception{
        if(validations.hasErrors()){
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            sectionService.save(data);
            return new ResponseEntity<>(new MessageDTO("Section saved"), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody SectionsDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            sectionService.update(data, id);
            return new ResponseEntity<>(new MessageDTO("Section updated"), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            sectionService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Section deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) Integer categoryId,
                                    @RequestParam(required = false) String lang){
        if(id != null && lang != null){
            return new ResponseEntity<>(sectionService.findById(id,lang), HttpStatus.OK);
        }
        else if(categoryId != null && lang != null){
            return new ResponseEntity<>(sectionService.findByCategory(categoryId,lang), HttpStatus.OK);
        }
        else if(lang != null){
            return new ResponseEntity<>(sectionService.findByLanguage(lang), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(sectionService.getAll(), HttpStatus.OK);
        }
    }
}
