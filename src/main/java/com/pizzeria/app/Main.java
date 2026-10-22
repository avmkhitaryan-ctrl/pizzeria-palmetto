package com.pizzeria.app;

import com.pizzeria.model.Customer;
import com.pizzeria.model.Order;
import com.pizzeria.model.Pizza;

import com.pizzeria.repository.CustomerFileRepository;
import com.pizzeria.repository.CustomerRepository;
import com.pizzeria.repository.OrderFileRepository;
import com.pizzeria.repository.OrderRepository;

import com.pizzeria.service.OrderService;
import com.pizzeria.service.OrderServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        CustomerRepository customerRepository =
                CustomerFileRepository.getInstance();

        OrderRepository orderRepository =
                OrderFileRepository.getInstance();

        OrderService orderService =
                new OrderServiceImpl(customerRepository, orderRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Pizzeria Palmetto ===");
            System.out.println("1. Register a new customer");
            System.out.println("2. Order as an existing customer");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            Customer customer;

            if (choice.equals("1")) {
                customer = registerCustomer(scanner, customerRepository);

                System.out.println("\nRegistered customer:");
                System.out.println(customer);

            } else if (choice.equals("2")) {
                System.out.print("Enter your username: ");
                String username = scanner.nextLine().trim();

                customer = customerRepository
                        .findCustomerByUsername(username)
                        .orElse(null);

                if (customer == null) {
                    System.out.println(
                            "Customer not found. Please register first."
                    );
                    return;
                }

                System.out.println("\nWelcome back!");
                System.out.println(customer);

            } else {
                System.out.println("Invalid option.");
                return;
            }

            createOrder(scanner, customer, orderService, orderRepository);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.out.println("Operation failed: " + exception.getMessage());
        }
    }


    private static Customer registerCustomer(
            Scanner scanner,
            CustomerRepository repository) {

        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();

        Customer customer = new Customer(fullName, username);
        repository.registerCustomer(customer);

        return customer;
    }

    private static void createOrder(
            Scanner scanner,
            Customer customer,
            OrderService orderService,
            OrderRepository orderRepository) {

        Map<Pizza, Integer> pizzas = new HashMap<>();

        while (true) {
            System.out.print("Enter pizza name (or 'done' to finish): ");
            String pizzaName = scanner.nextLine().trim();

            if (pizzaName.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter pizza price: ");
            double price;

            try {
                price = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Invalid price. Try again.");
                continue;
            }

            if (!Double.isFinite(price) || price <= 0) {
                System.out.println("Price must be a positive number.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity;

            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Invalid quantity. Try again.");
                continue;
            }

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                continue;
            }

            Pizza pizza = new Pizza(pizzaName, price);
            pizzas.merge(pizza, quantity, Integer::sum);
        }

        if (pizzas.isEmpty()) {
            System.out.println("No pizzas added. Order was not created.");
            return;
        }

        long orderId = orderService.orderPizza(
                customer.getUsername(),
                pizzas
        );

        Order order = new Order(customer.getCustomerId(), pizzas);
        order.setOrderId(orderId);

        System.out.println("\n=== Order saved successfully ===");
        System.out.println(order);
    }
}
