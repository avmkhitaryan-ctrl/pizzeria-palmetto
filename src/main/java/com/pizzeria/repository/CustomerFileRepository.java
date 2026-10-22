
package com.pizzeria.repository;

import com.pizzeria.common.JsonWriter;
import com.pizzeria.model.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CustomerFileRepository implements CustomerRepository {

    private static final String FILE_NAME = "data/customers.json";

    private static final CustomerFileRepository INSTANCE =
            new CustomerFileRepository();

    private final Map<String, Customer> customers = new HashMap<>();

    private long nextCustomerId = 1;

    private final JsonWriter jsonWriter = JsonWriter.getInstance();

    private CustomerFileRepository() {
        loadCustomers();
    }

    public static CustomerFileRepository getInstance() {
        return INSTANCE;
    }

    private void loadCustomers() {
        try {
            JSONArray array = jsonWriter.readArray(FILE_NAME);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                Customer customer = new Customer(
                        json.getString("fullName"),
                        json.getString("username")
                );

                long id = json.getLong("customerId");
                customer.setCustomerId(id);

                customers.put(customer.getUsername(), customer);
                nextCustomerId = Math.max(nextCustomerId, id + 1);
            }
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Cannot load customers from " + FILE_NAME,
                    exception
            );
        }
    }

    @Override
    public synchronized String registerCustomer(Customer customer) {
        String username = customer.getUsername();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (customers.containsKey(username)) {
            throw new IllegalArgumentException(
                    "Username already exists: " + username
            );
        }

        customer.setCustomerId(nextCustomerId++);
        customers.put(username, customer);

        try {
            saveCustomers();
        } catch (IOException exception) {
            customers.remove(username);
            nextCustomerId--;
            throw new IllegalStateException(
                    "Cannot save customer to " + FILE_NAME,
                    exception
            );
        }

        return username;
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        return Optional.ofNullable(customers.get(username));
    }

    private void saveCustomers() throws IOException {
        JSONArray array = new JSONArray();

        for (Customer customer : customers.values()) {
            JSONObject json = new JSONObject();
            json.put("customerId", customer.getCustomerId());
            json.put("fullName", customer.getFullName());
            json.put("username", customer.getUsername());

            array.put(json);
        }

        jsonWriter.writeArray(FILE_NAME, array);
    }
}
