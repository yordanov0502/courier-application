package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.data.models.enums.status.StatusType;
import bg.tu_varna.sit.repository.interfaces.OrderRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            String jpql = "SELECT o FROM Order o WHERE customer.id= '" + customerId + "'" +
                    " ORDER BY (\n" +
                    "    CASE o.status.statusType\n" +
                    "    \n" +
                    "    WHEN 'PENDING_COURIER'\n" +
                    "    THEN 1\n" +
                    "    \n" +
                    "    WHEN 'IN_PROCESS'\n" +
                    "    THEN 2\n" +
                    "    \n" +
                    "    WHEN 'DELIVERED'\n" +
                    "    THEN 3\n" +
                    "    END\n" +
                    ") ASC";
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
            String jpql = "SELECT o FROM Order o"+
                    " ORDER BY (\n" +
                    "    CASE o.status.statusType\n" +
                    "    \n" +
                    "    WHEN 'PENDING_COURIER'\n" +
                    "    THEN 1\n" +
                    "    \n" +
                    "    WHEN 'IN_PROCESS'\n" +
                    "    THEN 2\n" +
                    "    \n" +
                    "    WHEN 'DELIVERED'\n" +
                    "    THEN 3\n" +
                    "    END\n" +
                    ") ASC";
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

    @Override
    public List<Order> getOrdersOfCustomerFromLast5Days(Integer customerId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try {
            Date fiveDaysAgo = DateUtils.addDays(new Date(), -5);
            String jpql = "SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.createdAt >= :fiveDaysAgo"+
                    " ORDER BY (\n" +
                    "    CASE o.status.statusType\n" +
                    "    \n" +
                    "    WHEN 'PENDING_COURIER'\n" +
                    "    THEN 1\n" +
                    "    \n" +
                    "    WHEN 'IN_PROCESS'\n" +
                    "    THEN 2\n" +
                    "    \n" +
                    "    WHEN 'DELIVERED'\n" +
                    "    THEN 3\n" +
                    "    END\n" +
                    ") ASC";

            orders.addAll(session.createQuery(jpql, Order.class)
                    .setParameter("customerId", customerId)
                    .setParameter("fiveDaysAgo", fiveDaysAgo, TemporalType.DATE)
                    .getResultList());
            transaction.commit();
            log.info("Got orders of the customer from the last 5 days successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Error getting orders of the customer from the last 5 days: " + e.getMessage());
        } finally {
            session.close();
        }
        return orders;
    }

    @Override
    public List<Order> getAllPendingOrdersOfCourier(Integer courierId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            String jpql = "SELECT o FROM Order o WHERE courier.id= '" + courierId + "' AND status.statusType= 'PENDING_COURIER'";
            orders.addAll(session.createQuery(jpql, Order.class).getResultList());
            transaction.commit();
            log.info("Got all orders of courier with ID="+courierId+" which are with status 'PENDING_COURIER' successfully.");
        } catch (Exception e) {
            log.error("Get all orders of courier with ID="+courierId+" which are with status 'PENDING_COURIER' error: " + e.getMessage());
        } finally {
            session.close();
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersOfCustomerAfterDate(Integer customerId, Date date) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.createdAt >= :date " +
                    "ORDER BY (\n" +
                    "    CASE o.status.statusType\n" +
                    "    WHEN 'PENDING_COURIER'\n" +
                    "    THEN 1\n" +
                    "    WHEN 'IN_PROCESS'\n" +
                    "    THEN 2\n" +
                    "    WHEN 'DELIVERED'\n" +
                    "    THEN 3\n" +
                    "    END\n" +
                    ") ASC";

            orders.addAll(session.createQuery(jpql, Order.class)
                    .setParameter("customerId", customerId)
                    .setParameter("date", date, TemporalType.DATE)
                    .getResultList());
            transaction.commit();
            log.info("Got orders of the customer from the specified date successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Error getting orders of the customer from the specified date: " + e.getMessage());
        } finally {
            session.close();
        }
        return orders;
    }

    @Override
    public int[] getMonthlyOrdersOfCustomer(Integer customerId) {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);

        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        int[] monthlyOrders = new int[12];
        try
        {
            String jpql = "SELECT MONTH(o.createdAt), COUNT(o) FROM Order o " +
                    "WHERE o.customer.id = :customerId AND YEAR(o.createdAt) = :currentYear " +
                    "GROUP BY MONTH(o.createdAt) ORDER BY MONTH(o.createdAt)";
            List<Object[]> results = session.createQuery(jpql, Object[].class)
                    .setParameter("customerId", customerId)
                    .setParameter("currentYear", currentYear)
                    .getResultList();
            for (Object[] result : results)
            {
                int month = (Integer) result[0];
                long count = (Long) result[1];
                monthlyOrders[month - 1] = (int) count;
            }
            transaction.commit();
            log.info("Got monthly order counts for customer with ID=" + customerId + " successfully.");
        }
        catch (Exception e)
        {
            log.error("Error getting monthly order counts for customer with ID=" + customerId + ": " + e.getMessage());
            transaction.rollback();
        }
        finally
        {
            session.close();
        }
        return monthlyOrders;
    }

    @Override
    public int[] getOrdersOfCourierWithDifferentStatuses(Integer courierId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        int[] statusCounts = new int[3];
        try
        {
            String jpql = "SELECT o.status.statusType, COUNT(o) FROM Order o " +
                    "WHERE o.courier.id = :courierId " +
                    "GROUP BY o.status.statusType";
            List<Object[]> results = session.createQuery(jpql, Object[].class)
                    .setParameter("courierId", courierId)
                    .getResultList();
            for (Object[] result : results)
            {
                StatusType statusType = (StatusType) result[0];
                long count = (Long) result[1];
                switch (statusType)
                {
                    case PENDING_COURIER -> statusCounts[0] = (int) count;
                    case IN_PROCESS -> statusCounts[1] = (int) count;
                    case DELIVERED -> statusCounts[2] = (int) count;
                }
            }
            transaction.commit();
            log.info("Got order counts by status for courier with ID=" + courierId + " successfully.");
        }
        catch (Exception e)
        {
            log.error("Error getting order counts by status for courier with ID=" + courierId + ": " + e.getMessage());
            transaction.rollback();
        }
        finally
        {
            session.close();
        }
        return statusCounts;
    }

}
