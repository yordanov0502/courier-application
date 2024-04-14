package bg.tu_varna.sit.presentation.customer;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.*;
import bg.tu_varna.sit.data.models.enums.status.StatusType;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.CourierService;
import bg.tu_varna.sit.service.OrderService;
import bg.tu_varna.sit.service.StatusService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerOrders extends JFrame implements View {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable tableOrders;
    private JButton buttonOrders;
    private JButton buttonQueries;
    private JButton buttonGraphic;
    private JButton buttonAddOrder;
    private JButton buttonLogout;
    private JComboBox comboBox1;
    private JLabel label1;
    private JLabel label2;
    private JTextField textFieldInfo;

    private final OrderService orderService = OrderService.getInstance();
    private final CourierService courierService = CourierService.getInstance();
    private final StatusService statusService = StatusService.getInstance();
    private Customer customer;

    public CustomerOrders(Customer customer)
    {
        this.customer = customer;
    }

    @Override
    public void open() {
        setContentPane(panel);
        buttonOrders.setEnabled(false);
        comboBox1.setPreferredSize(new Dimension(40,40));
        buttonQueries.setPreferredSize(new Dimension(40, 40));
        buttonGraphic.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        textFieldInfo.setPreferredSize(new Dimension(40,40));
        setTitle("Клиент[Списък с поръчки]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableOrders.setBackground(Color.LIGHT_GRAY);
        refreshTableData();

        buttonAddOrder.setPreferredSize(new Dimension(50,50));
        buttonAddOrder.addActionListener(action -> addNewOrder());

        //buttonQueries.addMouseListener(new Navigator(this, new CustomerQueries(customer)));
        //buttonGraphic.addMouseListener(new Navigator(this, new CustomerGraphic(customer)));

        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.CUSTOMER)));

        java.util.List<Courier> courierList = courierService.getAllCouriers();
        for (Courier courier : courierList) {
            comboBox1.addItem(courier.getId() + "/" + courier.getName() + "/" + courier.getOffice().getCity()+"/"+courier.getOffice().getOfficeName());
        }
        comboBox1.setSelectedItem(null);
    }

    private void addNewOrder(){

        String orderInformation = textFieldInfo.getText();

        if(comboBox1.getSelectedItem()==null)
        {
            String [] parts = customer.getDeliveryAddress().split("/");
            String city = parts[0];
            String officeName = parts[1];
            Courier courier = courierService.getCourierByOfficeAddress(city,officeName);

            Status newStatus = statusService.addNewStatus(new Status(null, StatusType.PENDING_COURIER,(orderInformation.isEmpty()) ? "-" : orderInformation));
            if(orderService.addNewOrder(new Order(null,null,this.customer,courier,newStatus)))
            {
                JOptionPane.showMessageDialog(panel, "Успешно добавихте нова поръчка.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
            else {JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
        }
        else
        {
            String selectedItem = (String) comboBox1.getSelectedItem();
            String [] parts = selectedItem.split("/");
            Integer courierId = Integer.parseInt(parts[0]);
            Courier courier = courierService.getCourierById(courierId);
            Status newStatus = statusService.addNewStatus(new Status(null, StatusType.PENDING_COURIER,(orderInformation.isEmpty()) ? "-" : orderInformation));
            if(orderService.addNewOrder(new Order(null,null,this.customer,courier,newStatus)))
            {
                JOptionPane.showMessageDialog(panel, "Успешно добавихте нова поръчка.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
            else {JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
        }
    }

    private  Object[][] getOrdersOfUser() {
        List<Order> orders = orderService.getOrdersOfCustomer(customer.getId());

        Object[][] data = new Object[orders.size()][7];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = order.getCustomer().getId()+"/"+order.getCustomer().getName();
            data[i][2] = order.getCourier().getId()+"/"+order.getCourier().getName();
            data[i][3] = getStatus(order.getStatus());
            data[i][4] = order.getStatus().getAdditionalInformation();
        }

        return data;
    }

    private String getStatus(Status status){
        switch (status.getStatusType()){
            case PENDING_COURIER -> {return "Чака куриер";}
            case IN_PROCESS -> {return "В процес на доставка";}
            case DELIVERED -> {return "Доставена";}
            default -> {return "Грешка";}
        }
    }

    @Override
    public void close() {
        dispose();
    }

    private void refreshTableData() {
        comboBox1.setSelectedItem(null);
        textFieldInfo.setText("");

        String[] columnNames = {"Номер", "Клиент", "Куриер", "Статус", "Информация"};
        Object[][] data = getOrdersOfUser();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 2 && column != 3 && column != 4;
            }
        };
        tableOrders.setModel(model);
    }
}
