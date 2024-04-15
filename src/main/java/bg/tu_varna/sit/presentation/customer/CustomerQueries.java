package bg.tu_varna.sit.presentation.customer;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Order;
import bg.tu_varna.sit.data.models.entities.Status;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CustomerQueries extends JFrame implements View {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable tableOrders;
    private JButton buttonOrders;
    private JButton buttonQueries;
    private JButton buttonGraphic;
    private JButton buttonLogout;
    private JButton buttonLast5Days;
    private JButton buttonAfterDate;
    private JLabel label4;
    private JTextField textFieldDate;
    private final OrderService orderService = OrderService.getInstance();

    private Customer customer;

    public CustomerQueries(Customer customer){
        this.customer=customer;
    }

    @Override
    public void open() {
        setContentPane(panel);
        buttonQueries.setEnabled(false);
        buttonQueries.setPreferredSize(new Dimension(40, 40));
        buttonGraphic.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Клиент[Справки]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableOrders.setBackground(Color.LIGHT_GRAY);

        textFieldDate.setPreferredSize(new Dimension(40,40));

        buttonLast5Days.setPreferredSize(new Dimension(50,50));
        buttonLast5Days.addActionListener(action -> refreshTableData1());

        buttonAfterDate.setPreferredSize(new Dimension(50,50));
        buttonAfterDate.addActionListener(action -> performSearchForOrdersOfCustomerAfterDate(textFieldDate.getText()));

        buttonOrders.addMouseListener(new Navigator(this, new CustomerOrders(customer)));
        //buttonGraphic.addMouseListener(new Navigator(this, new CustomerGraphic(customer)));

        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.CUSTOMER)));

        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    private void performSearchForOrdersOfCustomerAfterDate(String dateValue){
        if(!dateValue.isEmpty())
        {
            try
            {
                refreshTableData2(LocalDate.parse(textFieldDate.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(panel, "Неправилен формат на дата. Форматът е: (година-месец-ден)", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
        }
       else{ JOptionPane.showMessageDialog(panel, "Моля въведете дата (година-месец-ден).", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    @Override
    public void close() {dispose();}

    private String getStatus(Status status){
        switch (status.getStatusType()){
            case PENDING_COURIER -> {return "Чака куриер";}
            case IN_PROCESS -> {return "В процес на доставка";}
            case DELIVERED -> {return "Доставена";}
            default -> {return "Грешка";}
        }
    }

    private  Object[][] getOrdersFromLast5Days() {
        List<Order> orders = orderService.getOrdersOfCustomerFromLast5Days(customer.getId());

        Object[][] data = new Object[orders.size()][7];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = order.getCreatedAt();
            data[i][2] = order.getCustomer().getId()+"/"+order.getCustomer().getName();
            data[i][3] = order.getCourier().getId()+"/"+order.getCourier().getName();
            data[i][4] = getStatus(order.getStatus());
            data[i][5] = order.getCourier().getOffice().getCity() + "/" + order.getCourier().getOffice().getOfficeName();
            data[i][6] = order.getStatus().getAdditionalInformation();
        }

        return data;
    }

    private void refreshTableData1() {
        textFieldDate.setText("");

        String[] columnNames = {"Номер","Дата", "Клиент", "Куриер", "Статус", "Адрес", "Информация"};
        Object[][] data = getOrdersFromLast5Days();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 2 && column != 3 && column != 4 && column != 5 && column != 6;
            }
        };
        tableOrders.setModel(model);
    }

    private  Object[][] getOrdersAfterDate(Date date) {
        List<Order> orders = orderService.getOrdersOfCustomerAfterDate(customer.getId(),date);

        Object[][] data = new Object[orders.size()][7];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = order.getCreatedAt();
            data[i][2] = order.getCustomer().getId()+"/"+order.getCustomer().getName();
            data[i][3] = order.getCourier().getId()+"/"+order.getCourier().getName();
            data[i][4] = getStatus(order.getStatus());
            data[i][5] = order.getCourier().getOffice().getCity() + "/" + order.getCourier().getOffice().getOfficeName();
            data[i][6] = order.getStatus().getAdditionalInformation();
        }

        return data;
    }

    private void refreshTableData2(LocalDate localDate) {

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String[] columnNames = {"Номер","Дата", "Клиент", "Куриер", "Статус", "Адрес", "Информация"};
        Object[][] data = getOrdersAfterDate(date);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 2 && column != 3 && column != 4 && column != 5 && column != 6;
            }
        };
        tableOrders.setModel(model);
    }
}
