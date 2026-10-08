package com.pizzeria.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Customer customer;
    private final List<Pizza> pizzas = new ArrayList<>();

    public Order(Customer customer) {
        this.customer = customer;
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    public double getTotal() {
        return pizzas.stream()
                .mapToDouble(Pizza::getPrice)
                .sum();
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }
}