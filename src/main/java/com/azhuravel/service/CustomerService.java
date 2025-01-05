package com.azhuravel.service;

import com.azhuravel.exception.DuplicateResourceException;
import com.azhuravel.exception.RequestValidationException;
import com.azhuravel.exception.ResourceNotFoundException;
import com.azhuravel.model.Customer;
import com.azhuravel.model.CustomerRegistrationRequest;
import com.azhuravel.repository.customer.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private CustomerDao customerDao;

    public List<Customer> getAllCustomers() {
        throw new UnsupportedOperationException();
    }

    public Customer getCustomer(Long id) {
        Customer newCustomer = new Customer();
        newCustomer.setId(id);
        return newCustomer;
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        throw new UnsupportedOperationException();
    }

    public void deleteCustomerById(Long id) {
        throw new UnsupportedOperationException();
    }

    public void updateCustomer(Long id, CustomerRegistrationRequest request) {
        throw new UnsupportedOperationException();
    }
}
