package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.CategoryDTO;
import com.hotelJB.hotelJB_API.services.CategoryService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody CategoryDTO data, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            categoryService.save(data);
            return new ResponseEntity<>(new MessageDTO("Subcategory created"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CategoryDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            categoryService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("Subcategory created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            categoryService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Subcategory deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) String lang){
        if(id != null && lang != null){
            return new ResponseEntity<>(categoryService.findById(id,lang), HttpStatus.OK);
        }
        else if(lang != null){
            return new ResponseEntity<>(categoryService.findByLanguage(lang), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
        }

    }
}
