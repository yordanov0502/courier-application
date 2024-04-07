package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.repository.implementations.CustomerRepositoryImpl;
import bg.tu_varna.sit.repository.implementations.OrderRepositoryImpl;
import org.apache.log4j.Logger;

import java.util.List;

public class OrderService {
    private static final Logger log = Logger.getLogger(OrderService.class);
    private final OrderService orderService = OrderService.getInstance();
    private final OrderRepositoryImpl orderRepository = OrderRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static OrderService getInstance() {
        return OrderService.OrderServiceHolder.INSTANCE;
    }

    private static class OrderServiceHolder {
        public static final OrderService INSTANCE = new OrderService();
    }

    public boolean addNewOrder(Order order){return  orderRepository.save(order);}

    public boolean deleteOrderById(Integer orderId){return orderRepository.deleteById(orderId);}

    public boolean updateOrder(Order order){return orderRepository.update(order);}

    public List<Order> getOrdersOfCustomer(Integer customerId){return orderRepository.getOrdersOfCustomer(customerId);}
}
