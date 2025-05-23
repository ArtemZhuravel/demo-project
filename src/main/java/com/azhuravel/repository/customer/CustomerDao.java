package com.azhuravel.repository.customer;

import com.azhuravel.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(Long id);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer customer);
}
