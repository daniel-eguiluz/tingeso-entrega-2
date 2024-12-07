package com.tutorial.studentservice.service;

import com.tutorial.studentservice.entity.Student;
import com.tutorial.studentservice.model.Pet;
import com.tutorial.studentservice.model.Book;
import com.tutorial.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student save(Student student) {
        Student studentNew = studentRepository.save(student);
        return studentNew;
    }

    public List<Book> getBooks(int studentId) {
        List<Book> books = restTemplate.getForObject("http://book-service/book/bystudent/" + studentId, List.class);
        return books;
    }

    public List<Pet> getPets(int studentId) {
        List<Pet> pets = restTemplate.getForObject("http://pet-service/pet/bystudent/" + studentId, List.class);
        return pets;
    }

    public Book saveBook(int studentId, Book book) {
        book.setStudentId(studentId);
        HttpEntity<Book> request = new HttpEntity<Book>(book);
        Book bookNew = restTemplate.postForObject("http://book-service/book", request, Book.class);
        return bookNew;
    }

    public Pet savePet(int studentId, Pet pet) {
        pet.setStudentId(studentId);
        HttpEntity<Pet> request = new HttpEntity<Pet>(pet);
        Pet petNew = restTemplate.postForObject("http://pet-service/pet", request, Pet.class);
        return petNew;
    }
}
