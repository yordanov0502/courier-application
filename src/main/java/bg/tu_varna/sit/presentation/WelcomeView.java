package bg.tu_varna.sit.presentation;

import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.common.navigation.Navigator;

import javax.swing.*;
import java.awt.*;

import static bg.tu_varna.sit.data.models.enums.user.Role.*;

public class WelcomeView extends JFrame implements View {
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;

    public WelcomeView() {}

    @Override
    public void open() {
        setContentPane(panel);
        button1.setPreferredSize(new Dimension(40, 40));
        button2.setPreferredSize(new Dimension(40, 40));
        button3.setPreferredSize(new Dimension(40, 40));
        setTitle("Начало");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        button1.addMouseListener(new Navigator(this, new LoginView(ADMIN)));
        button2.addMouseListener(new Navigator(this, new LoginView(COURIER)));
        button3.addMouseListener(new Navigator(this, new LoginView(CUSTOMER)));
    }

    @Override
    public void close() {
        dispose();
    }
}
