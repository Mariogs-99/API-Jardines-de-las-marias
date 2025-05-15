package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.CategoryClientViewDTO;
import com.hotelJB.hotelJB_API.models.dtos.CategoryRoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.services.CategoryRoomService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-room")
public class CategoryRoomController {

    @Autowired
    private CategoryRoomService categoryRoomService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody CategoryRoomDTO data, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            categoryRoomService.save(data);
            return new ResponseEntity<>(new MessageDTO("Category-Room created"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CategoryRoomDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            categoryRoomService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("Category-Room created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            categoryRoomService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Category-Room deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) String lang){
        if(id != null && lang != null){
            return new ResponseEntity<>(categoryRoomService.findById(id, lang), HttpStatus.OK);
        }
        else if(lang != null){
            return new ResponseEntity<>(categoryRoomService.findByLanguage(lang), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(categoryRoomService.getAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/public-view")
    public ResponseEntity<List<CategoryClientViewDTO>> getClientView() {
        return ResponseEntity.ok(categoryRoomService.getCategoriesForClientView());
    }

}
