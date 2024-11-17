package com.azhuravel;

import com.azhuravel.customer.Customer;
import com.azhuravel.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpringBootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			var faker = new Faker();
			Random random = new Random();
			Name name = faker.name();
			Customer customer = new Customer(name.firstName() + " " + name.lastName(), name.firstName() + name.lastName() + "@example.com",
					random.nextInt(16, 99));
			List<Customer> customers = List.of(customer);
//			customerRepository.saveAll(customers);
		};
	}
}
