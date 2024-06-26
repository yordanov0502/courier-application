package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.repository.implementations.OrderRepositoryImpl;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class OrderService {
    private static final Logger log = Logger.getLogger(OrderService.class);
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

    public List<Order> getAllOrders() {return orderRepository.getAllOrders();}

    public Order getOrderById(Integer orderId) {return orderRepository.getOrderById(orderId);}

    public List<Order> getOrdersOfCustomerFromLast5Days(Integer customerId){return orderRepository.getOrdersOfCustomerFromLast5Days(customerId);}
    public List<Order> getAllPendingOrdersOfCourier(Integer courierId){return orderRepository.getAllPendingOrdersOfCourier(courierId);}

    public List<Order> getOrdersOfCustomerAfterDate(Integer customerId, Date date){return orderRepository.getOrdersOfCustomerAfterDate(customerId,date);}

    public int[] getMonthlyOrdersOfCustomer(Integer customerId){return orderRepository.getMonthlyOrdersOfCustomer(customerId);}

    public int[] getOrdersOfCourierWithDifferentStatuses(Integer courierId){return orderRepository.getOrdersOfCourierWithDifferentStatuses(courierId);}
}