package bg.tu_varna.sit.presentation.admin;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.*;
import bg.tu_varna.sit.data.models.enums.status.StatusType;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.CourierService;
import bg.tu_varna.sit.service.CustomerService;
import bg.tu_varna.sit.service.OrderService;
import bg.tu_varna.sit.service.StatusService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;

import static bg.tu_varna.sit.data.models.enums.status.StatusType.*;

public class AdminOrders extends JFrame implements View {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable tableOrders;
    private JButton buttonCouriers;
    private JButton buttonCustomers;
    private JButton buttonOrders;
    private JButton buttonLogout;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton buttonAddOrder;
    private JButton buttonUpdateOrder;
    private JButton buttonDeleteOrder;
    private JLabel label4;
    private JTextField textFieldInfo;
    private JButton buttonClear;

    private final OrderService orderService = OrderService.getInstance();
    private final CourierService courierService = CourierService.getInstance();
    private final CustomerService customerService = CustomerService.getInstance();
    private final StatusService statusService = StatusService.getInstance();

    private Integer idOfSelectedOrderForUpdate;

    @Override
    public void open() {
        setContentPane(panel);
        buttonOrders.setEnabled(false);
        textFieldInfo.setPreferredSize(new Dimension(40,40));
        comboBox1.setPreferredSize(new Dimension(40,40));
        comboBox2.setPreferredSize(new Dimension(40,40));
        comboBox3.setPreferredSize(new Dimension(40,40));
        buttonCouriers.setPreferredSize(new Dimension(40, 40));
        buttonCustomers.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Админстратор[Списък с поръчки]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.ADMIN)));

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableOrders.setBackground(Color.LIGHT_GRAY);
        refreshTableData();

        buttonAddOrder.setPreferredSize(new Dimension(50,50));
        buttonAddOrder.addActionListener(action -> addNewOrder());

        buttonUpdateOrder.setPreferredSize(new Dimension(50, 50));
        buttonUpdateOrder.addActionListener(action -> updateOrder());

        buttonDeleteOrder.setPreferredSize(new Dimension(50, 50));
        buttonDeleteOrder.addActionListener(action -> deleteOrder());

        buttonClear.setPreferredSize(new Dimension(50,50));
        buttonClear.addActionListener(action -> clear());

        buttonCouriers.addMouseListener(new Navigator(this, new AdminCouriers()));
        buttonCustomers.addMouseListener(new Navigator(this,new AdminCustomers()));

        List<Customer> customerList = customerService.getAllCustomers();
        for (Customer customer : customerList) {comboBox1.addItem(customer.getId()+"/"+customer.getName()+"/"+customer.getDeliveryAddress());}

        List<Courier> courierList = courierService.getAllCouriers();
        for (Courier courier: courierList) {comboBox2.addItem(courier.getId()+"/"+courier.getName()+"/"+courier.getOffice().getCity()+"/"+courier.getOffice().getOfficeName());}

        comboBox3.addItem("Чака куриер");
        comboBox3.addItem("В процес на доставка");
        comboBox3.addItem("Доставена");

        comboBox1.setSelectedItem(null);
        comboBox2.setSelectedItem(null);
        comboBox3.setSelectedItem(null);

        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel selectionModel = tableOrders.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {  //? This check prevents double events
                int selectedRow = tableOrders.getSelectedRow();
                if (selectedRow < 0)
                {
                    comboBox1.setSelectedItem(null);
                    comboBox2.setSelectedItem(null);
                    comboBox3.setSelectedItem(null);
                    textFieldInfo.setText("");
                    this.idOfSelectedOrderForUpdate = null;
                }
                else
                {
                    DefaultTableModel model = (DefaultTableModel) tableOrders.getModel();

                    this.idOfSelectedOrderForUpdate = (Integer) model.getValueAt(selectedRow, 0);

                    String column1 = (String) model.getValueAt(selectedRow, 1);
                    String[] parts1 = column1.split("/");
                    Integer customerId = Integer.parseInt(parts1[0]);

                    String column2 = (String) model.getValueAt(selectedRow, 2);
                    String[] parts2 = column2.split("/");
                    Integer courierId = Integer.parseInt(parts2[0]);

                    String status = (String) model.getValueAt(selectedRow, 3);
                    String information = (String) model.getValueAt(selectedRow, 5);

                    Customer customer = customerService.getCustomerById(customerId);
                    Courier courier = courierService.getCourierById(courierId);

                    comboBox1.setSelectedItem(findComboBoxItem(comboBox1, customer.getId()+"/"+customer.getName()+"/"+customer.getDeliveryAddress()));
                    comboBox2.setSelectedItem(findComboBoxItem(comboBox2, courier.getId()+"/"+courier.getName()+"/"+courier.getOffice().getCity()+"/"+courier.getOffice().getOfficeName()));
                    comboBox3.setSelectedItem(findComboBoxItem(comboBox3,status));
                    textFieldInfo.setText(information);
                }
            }
        });
    }



    private void addNewOrder(){

        String orderInformation = textFieldInfo.getText();

        if(comboBox1.getSelectedItem()!=null && comboBox2.getSelectedItem()!=null && comboBox3.getSelectedItem()!=null)
        {
            String selectedCustomer = (String) comboBox1.getSelectedItem();
            String [] comboBox1Parts = selectedCustomer.split("/");
            Integer customerId = Integer.parseInt(comboBox1Parts[0]);
            Customer customer = customerService.getCustomerById(customerId);

            String selectedCourier = (String) comboBox2.getSelectedItem();
            String [] comboBox2Parts = selectedCourier.split("/");
            Integer courierId = Integer.parseInt(comboBox2Parts[0]);
            Courier courier = courierService.getCourierById(courierId);

            String selectedStatus = (String) comboBox3.getSelectedItem();

            StatusType statusType = getStatus(selectedStatus);

            if(customer.getDeliveryAddress().equals(courier.getOffice().getCity()))
            {
                Status newStatus = statusService.addNewStatus(new Status(null, statusType,(orderInformation.isEmpty()) ? "-" : orderInformation));
                if(orderService.addNewOrder(new Order(null,null,customer,courier,newStatus)))
                {
                    JOptionPane.showMessageDialog(panel, "Успешно добавихте нова поръчка.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    textFieldInfo.setText("");
                }
                else {JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}

            }
            else
            {
                JOptionPane.showMessageDialog(panel, "Градовете на клиента и куриера са различни. Моля изберете подходящи съвпадения.", "Информация", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(panel, "Моля изберете данни от всички задължителни полета(Клиент,Куриер,Статус)", "Грешка", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Transactional
    private void updateOrder(){
        String orderInformation = textFieldInfo.getText();
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow >= 0)
        {
            String selectedCustomer = (String) comboBox1.getSelectedItem();
            String [] comboBox1Parts = selectedCustomer.split("/");
            Integer customerId = Integer.parseInt(comboBox1Parts[0]);
            Customer customer = customerService.getCustomerById(customerId);

            String selectedCourier = (String) comboBox2.getSelectedItem();
            String [] comboBox2Parts = selectedCourier.split("/");
            Integer courierId = Integer.parseInt(comboBox2Parts[0]);
            Courier courier = courierService.getCourierById(courierId);

            String selectedStatus = (String) comboBox3.getSelectedItem();

            StatusType selectedStatusType = getStatus(selectedStatus);

            if(customer.getDeliveryAddress().equals(courier.getOffice().getCity()))
            {
                Order oldOrder = orderService.getOrderById(this.idOfSelectedOrderForUpdate);

                Status updatedStatus = oldOrder.getStatus().toBuilder()
                        .statusType(selectedStatusType)
                        .additionalInformation(((orderInformation.isEmpty()) ? "-" : orderInformation))
                        .build();
                if(!statusService.updateStatus(updatedStatus))
                {
                    JOptionPane.showMessageDialog(panel, "Възникна грешка при актуализирането на статуса. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Order updatedOrder = oldOrder.toBuilder()
                            .customer(customer)
                            .courier(courier)
                            .status(updatedStatus)
                            .build();
                    if(orderService.updateOrder(updatedOrder))
                    {
                        JOptionPane.showMessageDialog(panel, "Успешно актуализирахте поръчка с ID["+this.idOfSelectedOrderForUpdate+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        refreshTableData();
                        textFieldInfo.setText("");
                        this.idOfSelectedOrderForUpdate=null;
                    }
                    else {JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}

                }
            }
            else
            {
                JOptionPane.showMessageDialog(panel, "Градовете на клиента и куриера са различни. Моля изберете подходящи съвпадения.", "Информация", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете поръчка", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    private void deleteOrder(){
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) tableOrders.getModel();
            Integer orderId = (Integer) model.getValueAt(selectedRow, 0);
            if(orderService.deleteOrderById(orderId))
            {
                JOptionPane.showMessageDialog(panel, "Успешно изтрихте поръчка с ID["+orderId+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
            else { JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете поръчка", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    private void clear(){
        tableOrders.clearSelection();
        comboBox1.setSelectedItem(null);
        comboBox2.setSelectedItem(null);
        comboBox3.setSelectedItem(null);
        textFieldInfo.setText("");
        this.idOfSelectedOrderForUpdate=null;
    }

    @Override
    public void close() {
        dispose();
    }

    private String getStatus(Status status){
        switch (status.getStatusType()){
            case PENDING_COURIER -> {return "Чака куриер";}
            case IN_PROCESS -> {return "В процес на доставка";}
            case DELIVERED -> {return "Доставена";}
            default -> {return "Грешка";}
        }
    }

    private StatusType getStatus(String status){
        switch (status){
            case "Чака куриер" -> {return PENDING_COURIER;}
            case "В процес на доставка" -> {return IN_PROCESS;}
            case "Доставена" -> {return DELIVERED;}
            default -> {return null;}
        }
    }

    private Object findComboBoxItem(JComboBox comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).toString().equals(value)) {
                return comboBox.getItemAt(i);
            }
        }
        return null;
    }

    private  Object[][] getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        Object[][] data = new Object[orders.size()][7];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = order.getCustomer().getId()+"/"+order.getCustomer().getName();
            data[i][2] = order.getCourier().getId()+"/"+order.getCourier().getName();
            data[i][3] = getStatus(order.getStatus());
            data[i][4] = order.getCourier().getOffice().getCity() + "/" + order.getCourier().getOffice().getOfficeName();
            data[i][5] = order.getStatus().getAdditionalInformation();
        }

        return data;
    }

    private void refreshTableData() {
        comboBox1.setSelectedItem(null);
        comboBox2.setSelectedItem(null);
        comboBox3.setSelectedItem(null);
        this.idOfSelectedOrderForUpdate=null;

        String[] columnNames = {"Номер", "Клиент", "Куриер", "Статус", "Адрес", "Информация"};
        Object[][] data = getAllOrders();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 2 && column != 3 && column != 4 && column != 5;
            }
        };
        tableOrders.setModel(model);
    }
}
