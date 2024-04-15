package bg.tu_varna.sit.presentation.courier;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.data.models.entities.Status;
import bg.tu_varna.sit.data.models.enums.status.StatusType;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.CustomerService;
import bg.tu_varna.sit.service.OrderService;
import bg.tu_varna.sit.service.StatusService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;

import static bg.tu_varna.sit.data.models.enums.status.StatusType.*;

public class CourierDeliveriesByCustomer extends JFrame implements View {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable tableOrders;
    private JButton buttonOrdersPendingCourier;
    private JButton buttonOrdersByCustomer;
    private JButton buttonGraphic;
    private JButton buttonLogout;
    private JLabel label1;
    private JLabel label2;
    private JComboBox comboBox1;
    private JTextField textFieldInfo;
    private JButton buttonUpdateOrder;
    private JButton buttonClear;
    private JLabel label3;
    private JComboBox comboBox2;

    private final OrderService orderService = OrderService.getInstance();
    private final StatusService statusService = StatusService.getInstance();

    private final CustomerService customerService = CustomerService.getInstance();

    private Integer idOfSelectedOrderForUpdate;

    private Courier courier;

    public CourierDeliveriesByCustomer(Courier courier){
        this.courier=courier;
    }

    @Override
    public void open() {
        setContentPane(panel);
        buttonOrdersByCustomer.setEnabled(false);
        textFieldInfo.setPreferredSize(new Dimension(40,40));
        comboBox2.setPreferredSize(new Dimension(40,40));
        comboBox1.setPreferredSize(new Dimension(40,40));
        buttonOrdersPendingCourier.setPreferredSize(new Dimension(40, 40));
        buttonOrdersByCustomer.setPreferredSize(new Dimension(40,40));
        buttonGraphic.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Куриер[Списък с поръчки по конкретен клиент]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.COURIER)));

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableOrders.setBackground(Color.LIGHT_GRAY);

        buttonUpdateOrder.setPreferredSize(new Dimension(50,50));
        buttonUpdateOrder.addActionListener(action -> updateOrder());

        buttonClear.setPreferredSize(new Dimension(50, 50));
        buttonClear.addActionListener(action -> clear());

        buttonOrdersPendingCourier.addMouseListener(new Navigator(this, new CourierDeliveries(courier)));
        buttonGraphic.addMouseListener(new Navigator(this,new CourierGraphic(courier)));

        List<Customer> customerList = customerService.getAllCustomersWithOrdersByCourier(courier);
        for (Customer customer : customerList) {comboBox2.addItem(customer.getId()+"/"+customer.getName()+"/"+customer.getDeliveryAddress());}

        comboBox2.setSelectedItem(null);

        comboBox2.addActionListener(action -> {
            if (comboBox2.getSelectedItem() != null) {
                refreshTableData();
            }
        });

        comboBox1.addItem("Чака куриер");
        comboBox1.addItem("В процес на доставка");
        comboBox1.addItem("Доставена");

        comboBox1.setSelectedItem(null);

        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel selectionModel = tableOrders.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {  //? This check prevents double events
                int selectedRow = tableOrders.getSelectedRow();
                if (selectedRow < 0)
                {
                    comboBox1.setSelectedItem(null);
                    textFieldInfo.setText("");
                    this.idOfSelectedOrderForUpdate = null;
                }
                else
                {
                    DefaultTableModel model = (DefaultTableModel) tableOrders.getModel();

                    this.idOfSelectedOrderForUpdate = (Integer) model.getValueAt(selectedRow, 0);

                    String status = (String) model.getValueAt(selectedRow, 3);
                    String information = (String) model.getValueAt(selectedRow, 5);

                    comboBox1.setSelectedItem(findComboBoxItem(comboBox1,status));
                    textFieldInfo.setText(information);
                }
            }
        });
    }


    @Transactional
    private void updateOrder(){
        String orderInformation = textFieldInfo.getText();
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow >= 0)
        {
            String selectedStatus = (String) comboBox1.getSelectedItem();

            StatusType selectedStatusType = getStatus(selectedStatus);

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
        else {JOptionPane.showMessageDialog(panel, "Моля изберете поръчка", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    private void clear(){
        tableOrders.clearSelection();
        comboBox1.setSelectedItem(null);
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

    private  Object[][] getAllOrdersOfCustomer(Integer customerId) {
        List<Order> orders = orderService.getOrdersOfCustomer(customerId);

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
        textFieldInfo.setText("");
        this.idOfSelectedOrderForUpdate=null;

        if(comboBox2.getSelectedItem()!=null)
        {
            String selectedValue = (String) comboBox2.getSelectedItem();
            String [] parts = selectedValue.split("/");
            Integer customerId = Integer.parseInt(parts[0]);

            String[] columnNames = {"Номер", "Клиент", "Куриер", "Статус", "Адрес", "Информация"};
            Object[][] data = getAllOrdersOfCustomer(customerId);
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 0 && column != 1 && column != 2 && column != 3 && column != 4 && column != 5;
                }
            };
            tableOrders.setModel(model);
        }
        else
        {
            JOptionPane.showMessageDialog(panel, "Моля изберете клиент", "Грешка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
