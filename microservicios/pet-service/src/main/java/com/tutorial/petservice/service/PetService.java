package com.tutorial.petservice.service;

import com.tutorial.petservice.entity.Pet;
import com.tutorial.petservice.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    public Pet getPetById(int id) {
        return petRepository.findById(id).orElse(null);
    }

    public Pet save(Pet pet) {
        Pet petNew = petRepository.save(pet);
        return petNew;
    }

    public List<Pet> byStudentId(int studentId) {
        return petRepository.findByStudentId(studentId);
    }
}
