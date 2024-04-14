package bg.tu_varna.sit.presentation.admin;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Office;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.CustomerService;
import bg.tu_varna.sit.service.OfficeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminCustomers extends JFrame implements View {

    private JPanel panel;
    private JButton buttonCouriers;
    private JButton buttonCustomers;
    private JButton buttonOrders;
    private JTable tableCustomers;
    private JButton buttonLogout;
    private JScrollPane scrollPane;

    private JButton buttonUpdate;
    private JButton buttonDelete;
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JTextField textField3;
    private JButton buttonAddNewCustomer;
    private JComboBox comboBox1;
    private JLabel labelName;
    private JLabel labelUsername;
    private JLabel labelPassword;
    private JLabel labelPhone;
    private JLabel labelExperience;
    private JLabel labelOffice;

    private final CustomerService customerService = CustomerService.getInstance();
    private final OfficeService officeService = OfficeService.getInstance();
    @Override
    public void open() {
        setContentPane(panel);
        buttonCustomers.setEnabled(false);
        textField1.setPreferredSize(new Dimension(40,40));
        textField2.setPreferredSize(new Dimension(40,40));
        passwordField1.setPreferredSize(new Dimension(40,40));
        textField3.setPreferredSize(new Dimension(40,40));
        comboBox1.setPreferredSize(new Dimension(40,40));
        buttonAddNewCustomer.setPreferredSize(new Dimension(40,40));
        buttonCouriers.setPreferredSize(new Dimension(40, 40));
        buttonCustomers.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Админстратор[Списък с клиенти]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.ADMIN)));

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableCustomers.setBackground(Color.LIGHT_GRAY);
        refreshTableData();

        buttonAddNewCustomer.setPreferredSize(new Dimension(50,50));
        buttonAddNewCustomer.addActionListener(action -> addNewCustomer());

        buttonUpdate.setPreferredSize(new Dimension(50, 50));
        buttonUpdate.addActionListener(action -> updateCustomer());

        buttonDelete.setPreferredSize(new Dimension(50, 50));
        buttonDelete.addActionListener(action -> deleteCustomer());

        buttonCouriers.addMouseListener(new Navigator(this, new AdminCouriers()));

        List<String> citiesList = officeService.getAllDistinctCities();
        for (String city : citiesList) {
            comboBox1.addItem(city);
        }
        comboBox1.setSelectedItem(null);
    }

    private void addNewCustomer(){

        if(textField1.getText().isEmpty() || textField2.getText().isEmpty() || passwordField1.getPassword().length==0 || textField3.getText().isEmpty() || comboBox1.getSelectedItem()==null)
        {
            JOptionPane.showMessageDialog(panel, "Моля въведете данни във всички полета", "Грешка", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String selectedCity = (String) comboBox1.getSelectedItem();

            if(customerService.addNewCustomer(new Customer(null,textField1.getText(),textField2.getText(), Hasher.SHA512.hash(new String(passwordField1.getPassword())),textField3.getText(), selectedCity)))
            {
                JOptionPane.showMessageDialog(panel, "Успешно добавихте нов клиент.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                textField1.setText("");
                textField2.setText("");
                passwordField1.setText("");
                textField3.setText("");
                comboBox1.setSelectedItem(null);
                refreshTableData();
            }
            else
            {
                JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private  Object[][] getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        Object[][] data = new Object[customers.size()][7];
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getId();
            data[i][1] = customer.getName();
            data[i][2] = customer.getUsername();
            data[i][3] = customer.getPassword();
            data[i][4] = customer.getPhoneNumber();
            data[i][5] = customer.getDeliveryAddress();
        }

        return data;
    }

    private void updateCustomer(){
        int selectedRow = tableCustomers.getSelectedRow();
        if (selectedRow >= 0)
        {
            DefaultTableModel model = (DefaultTableModel) tableCustomers.getModel();

            Integer id = (Integer) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            String username = (String) model.getValueAt(selectedRow, 2);
            String password = (String) model.getValueAt(selectedRow, 3);
            String phoneNumber = (String) model.getValueAt(selectedRow, 4);
            String cityAddress = (String) model.getValueAt(selectedRow, 5);

            if(name.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || cityAddress.isEmpty())
            {
                JOptionPane.showMessageDialog(panel, "Моля въведете данни във всички полета", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                    if(officeService.cityHasOffice(cityAddress))
                    {
                        Customer updatedCustomer = new Customer(id, name, username, password, phoneNumber, cityAddress);

                        if(customerService.updateCustomer(updatedCustomer))
                        {
                            JOptionPane.showMessageDialog(panel, "Успешно актуализирахте клиент с ID["+id+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                            refreshTableData();
                        }
                        else { JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(panel, "Град: '"+cityAddress+"' няма офиси.", "Грешка", JOptionPane.ERROR_MESSAGE);
                        refreshTableData();
                    }


            }
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете клиент", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    private void deleteCustomer(){
        int selectedRow = tableCustomers.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) tableCustomers.getModel();
            System.out.println("Selected row data:");
            Integer customerId = (Integer) model.getValueAt(selectedRow, 0);
            if(customerService.deleteCustomerById(customerId))
            {
                JOptionPane.showMessageDialog(panel, "Успешно изтрихте клиент с ID["+customerId+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
            else { JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете клиент", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    @Override
    public void close() {
        dispose();
    }

    private void refreshTableData() {
        String[] columnNames = {"ID", "Име", "Потр. име", "Парола", "Тел. номер", "Адрес за доставка"};
        Object[][] data = getAllCustomers();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 3;
            }
        };
        tableCustomers.setModel(model);
    }
}
