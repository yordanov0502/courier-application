package bg.tu_varna.sit.presentation.courier;

import bg.tu_varna.sit.common.navigation.View;

import javax.swing.*;

public class CourierDeliveries extends JFrame implements View {
    private JPanel panel;

    @Override
    public void open() {
        setContentPane(panel);
//        textField1.setPreferredSize(new Dimension(40,40));
//        passwordField1.setPreferredSize(new Dimension(40,40));
//        button1.setPreferredSize(new Dimension(40, 40));
//        button2.setPreferredSize(new Dimension(40,40));
        setTitle("Куриер[Списък с доставки]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        label.setText("Вход за "+getRole(this.role)+".");
//        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
//        label.setHorizontalAlignment(JLabel.CENTER);
//        label.setVerticalAlignment(JLabel.CENTER);
//        button2.addMouseListener(new Navigator(this, new WelcomeView(),null));
//        errorLabel.setVisible(false);
//        errorLabel.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 16));
        //?if result from DB == null
    }

    @Override
    public void close() {
        dispose();
    }
}
