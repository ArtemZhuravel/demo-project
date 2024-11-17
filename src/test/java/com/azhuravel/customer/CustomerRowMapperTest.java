package com.azhuravel.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    private CustomerRowMapper underTest = new CustomerRowMapper();

    @Test
    void mapRow() throws SQLException {
        Customer expectedCustomerData = new Customer(1L, "testName", "testEmail", 2);

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(expectedCustomerData.getId());
        when(resultSet.getString("name")).thenReturn(expectedCustomerData.getName());
        when(resultSet.getString("email")).thenReturn(expectedCustomerData.getEmail());
        when(resultSet.getInt("age")).thenReturn(expectedCustomerData.getAge());

        Customer customer = underTest.mapRow(resultSet, 0);

        assertNotNull(customer);
        assertThat(customer.getId()).isEqualTo(expectedCustomerData.getId());
        assertThat(customer.getName()).isEqualTo(expectedCustomerData.getName());
        assertThat(customer.getEmail()).isEqualTo(expectedCustomerData.getEmail());
        assertThat(customer.getAge()).isEqualTo(expectedCustomerData.getAge());
    }
}