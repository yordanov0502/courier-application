package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.repository.implementations.CustomerRepositoryImpl;
import org.apache.log4j.Logger;

import java.util.List;

public class CustomerService {
    private static final Logger log = Logger.getLogger(CustomerService.class);
    private final CustomerRepositoryImpl customerRepository = CustomerRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static CustomerService getInstance() {
        return CustomerService.CustomerServiceHolder.INSTANCE;
    }

    private static class CustomerServiceHolder {
        public static final CustomerService INSTANCE = new CustomerService();
    }

    public Customer login(String username, String password){
        return customerRepository.checkCredentials(username,password);
    }

    public boolean addNewCustomer(Customer customer){return  customerRepository.save(customer);}

    public boolean deleteCustomerById(Integer customerId){return customerRepository.deleteById(customerId);}

    public boolean updateCustomer(Customer customer){return customerRepository.update(customer);}

    public List<Customer> getAllCustomers(){return customerRepository.getAllCustomers();}

    public Customer getCustomerById(Integer customerId) {return customerRepository.getCustomerById(customerId);}

    public List<Customer> getAllCustomersWithOrdersByCourier(Courier courier){return customerRepository.getAllCustomersWithOrdersByCourier(courier);}

}
