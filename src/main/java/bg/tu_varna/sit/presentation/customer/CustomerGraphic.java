package bg.tu_varna.sit.presentation.customer;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.enums.user.Role;
import bg.tu_varna.sit.presentation.LoginView;
import bg.tu_varna.sit.service.OrderService;
import com.intellij.uiDesigner.core.GridConstraints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class CustomerGraphic extends JFrame implements View {
    private JPanel panel;
    private JPanel panel2;
    private JButton buttonOrders;
    private JButton buttonQueries;
    private JButton buttonGraphic;
    private JButton buttonLogout;

    private final OrderService orderService = OrderService.getInstance();

    private Customer customer;

    public CustomerGraphic(Customer customer)
    {
        this.customer=customer;
    }

    @Override
    public void open() {
        setContentPane(panel);
        buttonGraphic.setEnabled(false);
        buttonQueries.setPreferredSize(new Dimension(40, 40));
        buttonGraphic.setPreferredSize(new Dimension(40,40));
        buttonOrders.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Клиент[графика за брой на поръчки извършени през цялата година]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        buttonOrders.addMouseListener(new Navigator(this, new CustomerOrders(customer)));
        buttonQueries.addMouseListener(new Navigator(this, new CustomerQueries(customer)));

        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.CUSTOMER)));

        int[] ordersOfCustomerForEachMonth = orderService.getMonthlyOrdersOfCustomer(customer.getId());

        DefaultPieDataset dataset = new DefaultPieDataset();

        String[] monthNames = {"Януари", "Февруари", "Март", "Април", "Май", "Юни",
                "Юли", "Август", "Септември", "Октомври", "Ноември", "Декември"};
        for (int i = 0; i < ordersOfCustomerForEachMonth.length; i++) {
            if (ordersOfCustomerForEachMonth[i] > 0) {
                dataset.setValue(monthNames[i] + " [" + ordersOfCustomerForEachMonth[i] + "]", ordersOfCustomerForEachMonth[i]);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Брой поръчки извършени през цялата година",
                dataset,
                true,
                true,
                false);

        PiePlot plot = (PiePlot) chart.getPlot();
        Color[] colors = {new Color(0, 188, 255), new Color(255, 0, 0), new Color(0, 255, 126),
                new Color(255, 255, 0), new Color(7, 114, 7), new Color(0, 5, 255),
                new Color(178, 89, 34), new Color(122, 118, 117), new Color(255, 165, 0),
                new Color(103, 43, 0), new Color(0, 255, 250), new Color(35, 38, 87)};
        for (int i = 0; i < monthNames.length; i++)
        {
            if (ordersOfCustomerForEachMonth[i] > 0)
            {
                plot.setSectionPaint(monthNames[i], colors[i]);
            }
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        panel2.add(chartPanel, new GridConstraints());
    }

    @Override
    public void close() {
        dispose();
    }

}
