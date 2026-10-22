
package com.pizzeria.repository;

import com.pizzeria.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryCustomerRepository
        implements CustomerRepository {

    private static final InMemoryCustomerRepository INSTANCE =
            new InMemoryCustomerRepository();

    private final Map<String, Customer> customers = new HashMap<>();

    private long nextCustomerId = 1;

    private InMemoryCustomerRepository() {
    }

    public static InMemoryCustomerRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public String registerCustomer(Customer customer) {
        Objects.requireNonNull(customer, "Customer cannot be null");

        String username = customer.getUsername();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username cannot be empty"
            );
        }

        if (customers.containsKey(username)) {
            throw new IllegalArgumentException(
                    "Username already exists: " + username
            );
        }

        customer.setCustomerId(nextCustomerId++);
        customers.put(username, customer);

        return username;
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        return Optional.ofNullable(customers.get(username));
    }
}
