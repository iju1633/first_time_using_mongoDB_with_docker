package com.example.mongo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository; // DAO layer 객체(db의 데이터 접근 가능)


    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }
}
