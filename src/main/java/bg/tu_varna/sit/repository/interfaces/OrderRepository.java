package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository<T> extends Repository<T> {
    boolean deleteById(Integer orderId);
    List<Order> getOrdersOfCustomer(Integer customerId);
    List<Order> getAllOrders();
    Order getOrderById(Integer orderId);
    List<Order> getOrdersOfCustomerFromLast5Days(Integer customerId);
    List<Order> getAllPendingOrdersOfCourier(Integer courierId);

    List<Order> getOrdersOfCustomerAfterDate(Integer customerId, Date date);
    int[] getMonthlyOrdersOfCustomer(Integer customerId);
    int[] getOrdersOfCourierWithDifferentStatuses(Integer courierId);
}
