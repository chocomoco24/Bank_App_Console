package repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


import domain.Customer;

public class CustomerRepository {
    private final Map<String, Customer> customersById = new HashMap<>();

    public List<Customer> findAll(){
        return new ArrayList<>(customersById.values());
    }

    public void save(Customer customer){
        customersById.put(customer.getId(), customer);
    }

}
