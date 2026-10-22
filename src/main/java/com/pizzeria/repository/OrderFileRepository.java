
package com.pizzeria.repository;

import com.pizzeria.common.JsonWriter;
import com.pizzeria.model.Order;
import com.pizzeria.model.Pizza;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class OrderFileRepository implements OrderRepository {

    private static final String FILE_NAME = "data/orders.json";

    private static final OrderFileRepository INSTANCE =
            new OrderFileRepository();

    private final Map<Long, Order> orders = new HashMap<>();

    private long nextOrderId = 1;

    private final JsonWriter jsonWriter = JsonWriter.getInstance();

    private OrderFileRepository() {
        loadOrders();
    }

    public static OrderFileRepository getInstance() {
        return INSTANCE;
    }

    private void loadOrders() {
        try {
            JSONArray array = jsonWriter.readArray(FILE_NAME);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                long orderId = json.getLong("orderId");
                long customerId = json.getLong("customerId");

                Map<Pizza, Integer> pizzas = new HashMap<>();
                JSONArray pizzaArray = json.getJSONArray("pizzas");

                for (int j = 0; j < pizzaArray.length(); j++) {
                    JSONObject pizzaJson = pizzaArray.getJSONObject(j);

                    Pizza pizza = new Pizza(
                            pizzaJson.getString("name"),
                            pizzaJson.getDouble("price")
                    );

                    int quantity = pizzaJson.getInt("quantity");
                    pizzas.put(pizza, quantity);
                }

                Order order = new Order(customerId, pizzas);
                order.setOrderId(orderId);

                orders.put(orderId, order);
                nextOrderId = Math.max(nextOrderId, orderId + 1);
            }
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Cannot load orders from " + FILE_NAME,
                    exception
            );
        }
    }

    @Override
    public synchronized long save(Order order) {
        long orderId = nextOrderId++;
        order.setOrderId(orderId);
        orders.put(orderId, order);

        try {
            saveOrders();
        } catch (IOException exception) {
            orders.remove(orderId);
            nextOrderId--;

            throw new IllegalStateException(
                    "Cannot save orders to " + FILE_NAME,
                    exception
            );
        }

        return orderId;
    }

    private void saveOrders() throws IOException {
        JSONArray array = new JSONArray();

        for (Order order : orders.values()) {
            JSONObject json = new JSONObject();
            json.put("orderId", order.getOrderId());
            json.put("customerId", order.getCustomerId());

            JSONArray pizzaArray = new JSONArray();

            for (Map.Entry<Pizza, Integer> entry
                    : order.getPizzas().entrySet()) {

                JSONObject pizzaJson = new JSONObject();
                pizzaJson.put("name", entry.getKey().getName());
                pizzaJson.put("price", entry.getKey().getPrice());
                pizzaJson.put("quantity", entry.getValue());

                pizzaArray.put(pizzaJson);
            }

            json.put("pizzas", pizzaArray);
            array.put(json);
        }

        jsonWriter.writeArray(FILE_NAME, array);
    }
}
