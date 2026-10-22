
package com.pizzeria.service;

import com.pizzeria.model.Customer;
import com.pizzeria.model.Order;
import com.pizzeria.model.Pizza;
import com.pizzeria.repository.CustomerRepository;
import com.pizzeria.repository.OrderRepository;

import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(
            CustomerRepository customerRepository,
            OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public long orderPizza(String username, Map<Pizza, Integer> pizzas) {
        Customer customer = customerRepository
                .findCustomerByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found: " + username
                        ));

        Order order = new Order(customer.getCustomerId(), pizzas);

        return orderRepository.save(order);
    }
}
