package com.tutorial.petservice.controller;

import com.tutorial.petservice.entity.Pet;
import com.tutorial.petservice.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAll() {
        List<Pet> pets = petService.getAll();
        if(pets.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getById(@PathVariable("id") int id) {
        Pet pet = petService.getPetById(id);
        if(pet == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pet);
    }

    @PostMapping()
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {
        Pet petNew = petService.save(pet);
        return ResponseEntity.ok(petNew);
    }

    @GetMapping("/bystudent/{studentId}")
    public ResponseEntity<List<Pet>> getByStudentId(@PathVariable("studentId") int studentId) {
        List<Pet> pets = petService.byStudentId(studentId);
        return ResponseEntity.ok(pets);
    }

}
