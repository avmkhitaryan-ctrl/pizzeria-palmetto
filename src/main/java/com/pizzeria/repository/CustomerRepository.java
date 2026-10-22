
package com.pizzeria.repository;

import com.pizzeria.model.Customer;

import java.util.Optional;

public interface CustomerRepository {

    String registerCustomer(Customer customer);

    Optional<Customer> findCustomerByUsername(String username);
}
