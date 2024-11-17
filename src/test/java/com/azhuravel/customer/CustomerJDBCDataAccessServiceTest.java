package com.azhuravel.customer;

import com.azhuravel.AbstractTestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), rowMapper);
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        List<Customer> customers = underTest.selectAllCustomers();

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId().equals(id)).isTrue();
            assertThat(c.getName().equals(customer.getName())).isTrue();
            assertThat(c.getEmail().equals(customer.getEmail())).isTrue();
            assertThat(c.getAge().equals(customer.getAge())).isTrue();
        });
    }

    @Test
    void willReturenEmptyWhenSelectCustomerById() {
        long id = -1;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        assertThat(underTest.existsPersonWithEmail(email)).isTrue();
    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        underTest.deleteCustomerById(id);

        assertThat(underTest.existsPersonWithEmail(email)).isFalse();
    }

    @Test
    void updateCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        customer.setId(id);
        customer.setName(customer.getName() + "_test");
        underTest.updateCustomer(customer);

        Optional<Customer> actualCustomer = underTest.selectCustomerById(id);

        assertThat(actualCustomer).hasValueSatisfying(c -> c.getName().equals(customer.getName()));
    }


    @Test
    void existsPersonWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        assertThat(underTest.existsPersonWithId(id)).isTrue();
    }
}