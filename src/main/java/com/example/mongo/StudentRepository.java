package com.example.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    // MongoRepository 상속받으면 saveAll, findAll, insert, findById등 다 사용가능

    Optional<Student> findStudentByEmail(String email);
}
