package com.pizzeria.app;

import com.pizzeria.model.Customer;
import com.pizzeria.model.Order;
import com.pizzeria.model.Pizza;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer(
                "Anna",
                "+374 00 000000"
        );

        Order order = new Order(customer);

        order.addPizza(new Pizza("Margherita", 8.5));
        order.addPizza(new Pizza("Pepperoni", 10.0));

        System.out.println(
                "Total for " + customer.getName() +
                        ": $" + order.getTotal()
        );
    }
}