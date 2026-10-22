
package com.pizzeria.model;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private Long orderId;
    private final Long customerId;
    private final Map<Pizza, Integer> pizzas;

    public Order(Long customerId, Map<Pizza, Integer> pizzas) {
        this.customerId = customerId;
        this.pizzas = new HashMap<>(pizzas);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Map<Pizza, Integer> getPizzas() {
        return pizzas;
    }

    public void addPizza(Pizza pizza, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                    "Pizza count must be greater than zero"
            );
        }

        pizzas.merge(pizza, count, Integer::sum);
    }

    public double getTotal() {
        return pizzas.entrySet()
                .stream()
                .mapToDouble(entry ->
                        entry.getKey().getPrice() * entry.getValue()
                )
                .sum();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", pizzas=" + pizzas +
                ", total=" + getTotal() +
                '}';
    }
}
