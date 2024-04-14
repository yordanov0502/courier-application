package bg.tu_varna.sit.presentation.admin;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.data.models.entities.Office;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.CourierService;
import bg.tu_varna.sit.service.OfficeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminCouriers extends JFrame implements View {


    private JPanel panel;
    private JButton buttonCouriers;
    private JButton buttonCustomers;
    private JButton buttonOrders;
    private JTable tableCouriers;
    private JButton buttonLogout;
    private JScrollPane scrollPane;
    private JButton buttonUpdate;
    private JButton buttonDelete;
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JTextField textField3;
    private JTextField textField4;
    private JButton buttonAddNewCourier;
    private JLabel labelName;
    private JLabel labelUsername;
    private JLabel labelPassword;
    private JLabel labelPhone;
    private JLabel labelExperience;
    private JLabel labelOffice;
    private JComboBox comboBox1;

    private final CourierService courierService = CourierService.getInstance();
    private final OfficeService officeService = OfficeService.getInstance();

    @Override
    public void open() {
        setContentPane(panel);
        buttonCouriers.setEnabled(false);
        textField1.setPreferredSize(new Dimension(40,40));
        textField2.setPreferredSize(new Dimension(40,40));
        passwordField1.setPreferredSize(new Dimension(40,40));
        textField3.setPreferredSize(new Dimension(40,40));
        textField4.setPreferredSize(new Dimension(40,40));
        comboBox1.setPreferredSize(new Dimension(40,40));
        buttonAddNewCourier.setPreferredSize(new Dimension(40,40));
        buttonCouriers.setPreferredSize(new Dimension(40, 40));
        buttonCustomers.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Админстратор[Списък с куриери]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.ADMIN)));

        scrollPane.setPreferredSize(new Dimension(600, 300));

        tableCouriers.setBackground(Color.LIGHT_GRAY);
        refreshTableData();

        buttonAddNewCourier.setPreferredSize(new Dimension(50,50));
        buttonAddNewCourier.addActionListener(action -> addNewCourier());

        buttonUpdate.setPreferredSize(new Dimension(50, 50));
        buttonUpdate.addActionListener(action -> updateCourier());

        buttonDelete.setPreferredSize(new Dimension(50, 50));
        buttonDelete.addActionListener(action -> deleteCourier());

        buttonCustomers.addMouseListener(new Navigator(this, new AdminCustomers()));
        buttonOrders.addMouseListener(new Navigator(this,new AdminOrders()));

        List<Office> officeList = officeService.getAllOffices();
        for (Office office : officeList) {
            comboBox1.addItem(office.getCity() + "/" + office.getOfficeName());
        }
        comboBox1.setSelectedItem(null);

        tableCouriers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void addNewCourier(){

        if(textField1.getText().isEmpty() || textField2.getText().isEmpty() || passwordField1.getPassword().length==0 || textField3.getText().isEmpty() || textField4.getText().isEmpty() || comboBox1.getSelectedItem()==null)
        {
            JOptionPane.showMessageDialog(panel, "Моля въведете данни във всички полета", "Грешка", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
                Integer yearsOfExperience = null;
                boolean isNumberFormatExceptionThrown = false;
                try{
                    yearsOfExperience = Integer.parseInt(textField4.getText());
                }
                catch (NumberFormatException exception)
                {
                    isNumberFormatExceptionThrown=true;
                }
                if(isNumberFormatExceptionThrown)
                {
                    JOptionPane.showMessageDialog(panel, "Моля въведете коректни данни за години опит", "Грешка", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    String selectedItem = (String) comboBox1.getSelectedItem();
                    String [] parts = selectedItem.split("/");
                    String city = parts[0];
                    String name = parts[1];

                    if(courierService.addNewCourier(new Courier(null,textField1.getText(),textField2.getText(), Hasher.SHA512.hash(new String(passwordField1.getPassword())),textField3.getText(),yearsOfExperience, officeService.getOfficeByCityAndName(city,name))))
                    {
                        JOptionPane.showMessageDialog(panel, "Успешно добавихте нов куриер.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        textField1.setText("");
                        textField2.setText("");
                        passwordField1.setText("");
                        textField3.setText("");
                        textField4.setText("");
                        comboBox1.setSelectedItem(null);
                        refreshTableData();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);
                    }
                }
        }
    }

    private  Object[][] getAllCouriers() {
        List<Courier> couriers = courierService.getAllCouriers();

        Object[][] data = new Object[couriers.size()][7];
        if (couriers.size() != 0)
        {
            for (int i = 0; i < couriers.size(); i++) {
                Courier courier = couriers.get(i);
                data[i][0] = courier.getId();
                data[i][1] = courier.getName();
                data[i][2] = courier.getUsername();
                data[i][3] = courier.getPassword();
                data[i][4] = courier.getPhoneNumber();
                data[i][5] = courier.getYearsExperience();
                data[i][6] = courier.getOffice().getCity()+"/"+courier.getOffice().getOfficeName();
            }

        }
        return data;

    }

    private void updateCourier(){
        int selectedRow = tableCouriers.getSelectedRow();
        if (selectedRow >= 0)
        {
            DefaultTableModel model = (DefaultTableModel) tableCouriers.getModel();

            Integer id = (Integer) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            String username = (String) model.getValueAt(selectedRow, 2);
            String password = (String) model.getValueAt(selectedRow, 3);
            String phoneNumber = (String) model.getValueAt(selectedRow, 4);

            boolean isNumberFormatExceptionThrown = false;
            //? checks the yearsOfExperience field in advance to prevent [NumberFormatException: For input string: ""]
            if(model.getValueAt(selectedRow, 5).toString().isEmpty())
            {
                JOptionPane.showMessageDialog(panel, "Моля въведете данни във всички полета", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                Integer yearsExperience = null;
                if(model.getValueAt(selectedRow, 5) instanceof Integer)
                {
                    yearsExperience = (Integer) model.getValueAt(selectedRow, 5);
                }
                if(model.getValueAt(selectedRow,5) instanceof String)
                {
                    try {
                        yearsExperience = Integer.parseInt((String)model.getValueAt(selectedRow, 5));
                    }
                    catch (NumberFormatException exception)
                    {
                        isNumberFormatExceptionThrown=true;
                    }
                }
                String officeData = (String) model.getValueAt(selectedRow, 6);

                if(isNumberFormatExceptionThrown)
                {
                    JOptionPane.showMessageDialog(panel, "Моля въведете коректни данни за години опит", "Грешка", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    if(name.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || officeData.isEmpty())
                    {
                        JOptionPane.showMessageDialog(panel, "Моля въведете данни във всички полета", "Грешка", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        String [] parts = officeData.split("/");
                        if (parts.length > 1) {
                            String city = parts[0];
                            String officeName = parts[1];
                            Office office = officeService.getOfficeByCityAndName(city,officeName);

                            if(office!=null)
                            {
                                Courier updatedCourier = new Courier(id, name, username, password, phoneNumber, yearsExperience, office);

                                if(courierService.updateCourier(updatedCourier))
                                {
                                    JOptionPane.showMessageDialog(panel, "Успешно актуализирахте куриер с ID["+id+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                                    refreshTableData();
                                }
                                else { JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(panel, "Офис: '"+city+"/"+officeName+"' не съществува. ", "Грешка", JOptionPane.ERROR_MESSAGE);
                                refreshTableData();
                            }

                        }
                        else
                        {
                            JOptionPane.showMessageDialog(panel, "Неправилен формат на данни за офис [Град/Име на офис]", "Грешка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете куриер", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    private void deleteCourier(){
        int selectedRow = tableCouriers.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) tableCouriers.getModel();
            Integer courierId = (Integer) model.getValueAt(selectedRow, 0);
            if(courierService.deleteCourierById(courierId))
            {
                JOptionPane.showMessageDialog(panel, "Успешно изтрихте куриер с ID["+courierId+"].", "Информация", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
            else { JOptionPane.showMessageDialog(panel, "Възникна грешка. Моля опитайте отново.", "Грешка", JOptionPane.ERROR_MESSAGE);}
        }
        else {JOptionPane.showMessageDialog(panel, "Моля изберете куриер", "Грешка", JOptionPane.ERROR_MESSAGE);}
    }

    @Override
    public void close() {
        dispose();
    }

    private void refreshTableData() {
        String[] columnNames = {"ID", "Име", "Потр. име", "Парола", "Тел. номер", "Години стаж", "Офис"};
        Object[][] data = getAllCouriers();
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 3;
            }
        };
        tableCouriers.setModel(model);
    }

}
