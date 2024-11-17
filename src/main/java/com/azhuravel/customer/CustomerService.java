package com.azhuravel.customer;

import com.azhuravel.exception.DuplicateResourceException;
import com.azhuravel.exception.RequestValidationException;
import com.azhuravel.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("No customer with id %s found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        if (customerDao.existsPersonWithEmail(request.email())) {
            throw new DuplicateResourceException("Email is already taken");
        }
        Customer customer = new Customer(request.name(), request.email(), request.age());
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Long id) {
        if (customerDao.existsPersonWithId(id)) {
            customerDao.deleteCustomerById(id);
            return;
        }
        throw new ResourceNotFoundException("Customer with id %s is not found".formatted(id));
    }

    public void updateCustomer(Long id, CustomerRegistrationRequest request) {
        Customer customer = getCustomer(id);
        boolean changes = false;
        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }
        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(request.email())) {
                throw new DuplicateResourceException("Email is already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }
        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("No changes to the customer");
        }
        customerDao.updateCustomer(customer);
    }
}
