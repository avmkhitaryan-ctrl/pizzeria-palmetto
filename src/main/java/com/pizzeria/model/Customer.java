
package com.pizzeria.model;

public class Customer {

    private long customerId;
    private final String fullName;
    private final String username;

    public Customer(String fullName, String username) {
        this.fullName = fullName;
        this.username = username;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
