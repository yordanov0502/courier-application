package bg.tu_varna.sit.presentation;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.UserEntity;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.admin.AdminCouriers;
import bg.tu_varna.sit.presentation.courier.CourierDeliveries;
import bg.tu_varna.sit.presentation.customer.CustomerOrders;
import bg.tu_varna.sit.service.AdminService;
import bg.tu_varna.sit.service.CourierService;
import bg.tu_varna.sit.service.CustomerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.tu_varna.sit.data.models.enums.user.Role.*;

public class LoginView extends JFrame implements View {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton button1;
    private JPanel panel;
    private JLabel label;
    private JButton button2;
    private JLabel errorLabel;
    private JLabel labelPassword;
    private JLabel labelUsername;


    private Role role;

    private final AdminService adminService = AdminService.getInstance();
    private final CourierService courierService = CourierService.getInstance();
    private final CustomerService customerService = CustomerService.getInstance();

    public LoginView(Role role) {this.role=role;}

    @Override
    public void open() {
        setContentPane(panel);
        textField1.setPreferredSize(new Dimension(40,40));
        passwordField1.setPreferredSize(new Dimension(40,40));
        button1.setPreferredSize(new Dimension(40, 40));
        button2.setPreferredSize(new Dimension(40,40));
        setTitle("Вход");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        label.setText("Вход за "+getRole(this.role)+".");
        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        errorLabel.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 16));
        errorLabel.setText("Невалидно потребителско име или парола");
        errorLabel.setForeground(Color.RED);

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(getRole().equals(ADMIN))
                {
                    if (checkCredentials(textField1.getText(), new String(passwordField1.getPassword()),getRole())!=null) {
                        new Navigator(getThis(), new AdminCouriers()).mouseClicked(e);
                    }
                    else {errorLabel.setVisible(true);}
                }
                if(getRole().equals(COURIER))
                {
                    if (checkCredentials(textField1.getText(), new String(passwordField1.getPassword()),getRole())!=null) {
                        new Navigator(getThis(), new CourierDeliveries()).mouseClicked(e);
                    }
                    else {errorLabel.setVisible(true);}
                }
                if(getRole().equals(CUSTOMER))
                {
                    Customer customer = (Customer) checkCredentials(textField1.getText(), new String(passwordField1.getPassword()),getRole());
                    if (customer!=null) {
                        new Navigator(getThis(), new CustomerOrders(customer)).mouseClicked(e);
                    }
                    else {errorLabel.setVisible(true);}
                }

            }
        });

        button2.addMouseListener(new Navigator(this, new WelcomeView()));

    }

    private LoginView getThis(){
        return this;
    }

    private Role getRole(){
        return this.role;
    }


    @Override
    public void close() {
       dispose();
    }



    private String getRole(Role role){
        switch (role){
            case ADMIN -> {return "администратори";}
            case COURIER -> {return "куриери";}
            case CUSTOMER -> {return "клиенти";}
            default -> {return null;}
        }
    }

    private UserEntity checkCredentials(String username, String password, Role role){
        switch (role){
            case ADMIN -> {return adminService.login(username,password);}
            case COURIER -> {return courierService.login(username,password);}
            case CUSTOMER -> {return customerService.login(username,password);}
            default -> {return null;}
        }

    }
}
