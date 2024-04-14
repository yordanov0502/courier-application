package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.repository.interfaces.CustomerRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository<Customer> {
    private static final Logger log = Logger.getLogger(CustomerRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static CustomerRepositoryImpl getInstance() {return CustomerRepositoryImpl.CustomerRepositoryHolder.INSTANCE;}

    private static class CustomerRepositoryHolder {
        public static final CustomerRepositoryImpl INSTANCE = new CustomerRepositoryImpl();
    }

    @Override
    public boolean save(Customer obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Customer added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Customer add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Customer obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Customer updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Customer update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Customer obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Customer deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Customer delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Customer checkCredentials(String username, String passwordInput) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = null;

        try{
            String jpql = "SELECT cu FROM Customer cu WHERE username = '" + username + "' AND password = '" + Hasher.SHA512.hash(passwordInput) + "'";
            customer = (Customer) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            if(customer != null) return customer;
            log.info("Got customer by username & password successfully.");
        } catch (Exception e) {
            log.error("Get customer by username & password error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Customer> customers = new ArrayList<>();
        try{
            String jpql = "SELECT c FROM Customer c";
            customers.addAll(session.createQuery(jpql, Customer.class).getResultList());
            transaction.commit();
            log.info("Got all customers successfully.");
        } catch (Exception e) {
            log.error("Get all customers error: " + e.getMessage());
        } finally {
            session.close();
        }
        return customers;
    }

    @Override
    public boolean deleteById(Integer customerId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Customer customer = session.get(Customer.class, customerId);
            if (customer != null) {
                session.delete(customer);
                transaction.commit();
                log.info("Customer with ID " + customerId + " deleted successfully.");
                return true;
            } else {
                log.info("No customer found with ID: " + customerId);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.info("Customer delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Customer getCustomerById(Integer customerId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = null;
        try{
            String jpql = "SELECT c FROM Customer c WHERE c.id = '"+customerId+"'";
            customer = session.createQuery(jpql, Customer.class).getSingleResult();
            transaction.commit();
            log.info("Got customer by id successfully.");
        } catch (Exception e) {
            log.error("Get customer by id error: " + e.getMessage());
        } finally {
            session.close();
        }
        return customer;
    }
}
