
package com.pizzeria.repository;

import com.pizzeria.model.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class InMemoryOrderRepository implements OrderRepository {

    private static final InMemoryOrderRepository INSTANCE =
            new InMemoryOrderRepository();

    private final Map<Long, Order> orders = new HashMap<>();

    private long nextOrderId = 1;

    private InMemoryOrderRepository() {
    }

    public static InMemoryOrderRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public long save(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");

        long orderId = nextOrderId++;
        order.setOrderId(orderId);
        orders.put(orderId, order);

        return orderId;
    }
}
