package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Office;
import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.repository.interfaces.OrderRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository<Order> {
    private static final Logger log = Logger.getLogger(OrderRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static OrderRepositoryImpl getInstance() {return OrderRepositoryImpl.OrderRepositoryHolder.INSTANCE;}

    private static class OrderRepositoryHolder {
        public static final OrderRepositoryImpl INSTANCE = new OrderRepositoryImpl();
    }

    @Override
    public boolean save(Order obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Order added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Order add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Order obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Order updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Order update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Order obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Order deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Order delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(Integer orderId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Order order = session.get(Order.class, orderId);
            if (order != null) {
                session.delete(order);
                transaction.commit();
                log.info("Order with ID " + orderId + " deleted successfully.");
                return true;
            } else {
                log.info("No order found with ID: " + orderId);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.info("Order delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Order> getOrdersOfCustomer(Integer customerId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            String jpql = "SELECT o FROM Order o WHERE customer.id= '" + customerId + "'";
            orders.addAll(session.createQuery(jpql, Order.class).getResultList());
            transaction.commit();
            log.info("Got all orders of customer with ID="+customerId+" successfully.");
        } catch (Exception e) {
            log.error("Get all orders of customer with ID="+customerId+" error: " + e.getMessage());
        } finally {
            session.close();
        }
        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            String jpql = "SELECT o FROM Order o";
            orders.addAll(session.createQuery(jpql, Order.class).getResultList());
            transaction.commit();
            log.info("Got all orders successfully.");
        } catch (Exception e) {
            log.error("Get all orders error: " + e.getMessage());
        } finally {
            session.close();
        }
        return orders;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Order order = null;
        try{
            String jpql = "SELECT o FROM Order o WHERE o.id = '"+orderId+"'";
            order = session.createQuery(jpql, Order.class).getSingleResult();
            transaction.commit();
            log.info("Got order by id successfully.");
        } catch (Exception e) {
            log.error("Get order by id error: " + e.getMessage());
        } finally {
            session.close();
        }
        return order;
    }

}
