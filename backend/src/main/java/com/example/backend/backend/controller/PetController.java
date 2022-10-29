package com.example.backend.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.backend.collections.Pet;
import com.example.backend.backend.service.PetService;

@RestController
public class PetController {
    
    @Autowired
    private PetService petService;
    

    // to post a pet 
    @PostMapping("/post/pet/{userId}")
    public ResponseEntity<?> postAPost(@PathVariable String userId,@ModelAttribute Pet pet,@RequestParam ("file") MultipartFile file){
     return ResponseEntity.ok(petService.postAPet(userId,pet,file));
    }

    // to get all posted pet of specific user
    @GetMapping("/get/all/posted/pet/{userId}")
    public ResponseEntity<?> getAllPostedPetOfASpecificUser(@PathVariable String userId){
       return ResponseEntity.ok(petService.getAllpostedPetOfASpecificUser(userId));
    }

    // get all posted pets
    @GetMapping("/get/all/posted/pets")
    public ResponseEntity<?> getAllPostedPets(){
      return ResponseEntity.ok(petService.getAllpostedPets());
    }
}