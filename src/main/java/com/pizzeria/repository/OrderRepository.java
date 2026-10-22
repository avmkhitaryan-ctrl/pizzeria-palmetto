
package com.pizzeria.repository;

import com.pizzeria.model.Order;

public interface OrderRepository {

    long save(Order order);
}
