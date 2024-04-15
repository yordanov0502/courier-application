package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Customer;

import java.util.List;

public interface CustomerRepository<T> extends Repository<T> {
    Customer checkCredentials(String username, String password);
    List<Customer> getAllCustomers();
    boolean deleteById(Integer customerId);
    Customer getCustomerById(Integer customerId);
    List<Customer> getAllCustomersWithOrdersByCourier(Courier courier);
}
