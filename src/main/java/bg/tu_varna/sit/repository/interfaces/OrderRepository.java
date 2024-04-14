package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Order;

import java.util.List;

public interface OrderRepository<T> extends Repository<T> {
    boolean deleteById(Integer orderId);
    List<Order> getOrdersOfCustomer(Integer customerId);
    List<Order> getAllOrders();
    Order getOrderById(Integer orderId);
}
