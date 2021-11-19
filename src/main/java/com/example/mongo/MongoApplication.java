package com.example.mongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class MongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
		return args -> {
			Address address = new Address(
				"South Korea",
				"Seoul",
				"NE9"
			);
			String email = "iju1633@gmail.com";

			Student student = new Student(
				"Eric",
				"Im",
				email,
				Gender.MALE,
				address, List.of("Computer Science", "Maths"),
				BigDecimal.TEN,
				LocalDateTime.now()
			);

			// usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

			// 훨씬 더 간편해짐!!
			repository.findStudentByEmail(email)
					.ifPresentOrElse(s -> {
						System.out.println(s + " already exists");
					}, () -> {
						System.out.println("Inserting student " + student);
						repository.insert(student);
					});

			// custom 쿼리문으로 데이터 중복 제거해서 추가하는 방법
			// interface에 optional로 findByEmail 추가!

		};
	}

	private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		// 해당 email로 찾은 쿼리가 1개보다 많으면 문제 있는 것임(고유한 것이므로)
		List<Student> students = mongoTemplate.find(query, Student.class);

		if(students.size() > 1){
			throw new IllegalStateException("found many students with email " + email);
		}

		// 해당 이메일로 찾은 쿼리강 없으면 추가하면 됨
		if(students.isEmpty()) {
			System.out.println("Inserting student " + student);
			repository.insert(student);
		} else {
			System.out.println(student + " already exists");
		}
	}
}
