package com.azhuravel.customer;

import com.azhuravel.exception.DuplicateResourceException;
import com.azhuravel.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //  When
        underTest.getAllCustomers();

        //  Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //  Given
        long id = 10;

        Customer customer = new Customer(id, "Test", "test@test.com", 40);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //  When
        Customer actual = underTest.getCustomer(id);

        //  Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willTrowWhenGetCustomerReturnsEmptyOptional() {
        //  Given
        long id = 10;

        Customer customer = new Customer(id, "Test", "test@test.com", 40);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //  When

        //  Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No customer with id %s found".formatted(id));
    }

    @Test
    void addCustomer() {
        //  Given
        String email = "test@test.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("testName", email, 19);

        //  When
        underTest.addCustomer(request);

        //  Then
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(captor.capture());

        Customer captoredCustomer = captor.getValue();

        assertThat(captoredCustomer.getId()).isNull();
        assertThat(captoredCustomer.getName()).isEqualTo(request.name());
        assertThat(captoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captoredCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsAddCustomer() {
        //  Given
        String email = "test@test.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("testName", email, 19);

        //  When
        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class);

        //  Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        //  Given
        Long id = 10L;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        //  When
        underTest.deleteCustomerById(id);

        //  Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerByIdWillThrow() {
        //  Given
        Long id = 10L;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        //  When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer with id %s is not found".formatted(id));

        //  Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerWillThrow() {
        //  Given
        Long id = 10L;
        String email = "test@test.com";
        Customer customer = new Customer(id, "testName", email, 19);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("testName1", email, 19);

        //  When
        underTest.updateCustomer(id, request);

        //  Then
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(captor.capture());

        Customer captoredCustomer = captor.getValue();

        assertThat(captoredCustomer.getId()).isEqualTo(customer.getId());
        assertThat(captoredCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captoredCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captoredCustomer.getAge()).isEqualTo(customer.getAge());

        assertThat(captoredCustomer.getName()).isEqualTo(request.name());
    }
}