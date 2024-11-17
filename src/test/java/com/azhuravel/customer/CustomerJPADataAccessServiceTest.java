package com.azhuravel.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    private AutoCloseable autoCloseable;

    @Mock private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //  When
        underTest.selectAllCustomers();

        //  Then
        verify(repository).findAll();
    }

    @Test
    void selectCustomerById() {
        //  Given
        Long id = 1L;

        //  When
        underTest.selectCustomerById(id);

        //  Then
        verify(repository).findById(id);
    }

    @Test
    void insertCustomer() {
        //  Given
        Customer customer = new Customer();

        //  When
        underTest.insertCustomer(customer);

        //  Then
        verify(repository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //  Given
        String email = "test@test.com";

        //  When
        underTest.existsPersonWithEmail(email);

        //  Then
        verify(repository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        //  Given
        Long id = 1L;

        //  When
        underTest.existsPersonWithId(id);

        //  Then
        verify(repository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //  Given
        Long id = 1L;

        //  When
        underTest.deleteCustomerById(id);

        //  Then
        verify(repository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //  Given
        Customer customer = new Customer();

        //  When
        underTest.updateCustomer(customer);

        //  Then
        verify(repository).save(customer);
    }
}