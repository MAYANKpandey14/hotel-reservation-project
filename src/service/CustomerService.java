package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CustomerService {
    private static final Map<String, Customer> customers = new HashMap<String, Customer>();

    private CustomerService() {
    }

    public static void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(email, firstName, lastName);
        if (customers.containsKey(email)) { //checks if the email given has already been used with an account or not.
            throw new IllegalArgumentException("Account already present with this Email address");
        } else {
            customers.put(email, customer); // adds a new customer
        }
    }

    public static Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public static Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
