package bg.tu_varna.sit.presentation.courier;

import bg.tu_varna.sit.common.navigation.Navigator;
import bg.tu_varna.sit.common.navigation.View;
import bg.tu_varna.sit.data.models.entities.Courier;
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

public class CourierGraphic extends JFrame implements View {
    private JPanel panel;
    private JPanel panel2;
    private JButton buttonOrdersPendingCourier;
    private JButton buttonOrdersByCustomer;
    private JButton buttonGraphic;
    private JButton buttonLogout;

    private final OrderService orderService = OrderService.getInstance();

    private Courier courier;

    public CourierGraphic(Courier courier)
    {
        this.courier=courier;
    }

    @Override
    public void open() {
        setContentPane(panel);
        buttonGraphic.setEnabled(false);
        buttonOrdersPendingCourier.setPreferredSize(new Dimension(40, 40));
        buttonGraphic.setPreferredSize(new Dimension(40,40));
        buttonOrdersByCustomer.setPreferredSize(new Dimension(40,40));
        buttonLogout.setPreferredSize(new Dimension(40,40));
        setTitle("Куриер[графика за брой поръчки със статус “Чака куриер”,  “В процес на доставка” и “Доставена”]");
        setBounds(270, 100, 1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        buttonOrdersPendingCourier.addMouseListener(new Navigator(this, new CourierDeliveries(courier)));
        buttonOrdersByCustomer.addMouseListener(new Navigator(this, new CourierDeliveriesByCustomer(courier)));

        buttonLogout.addMouseListener(new Navigator(this, new LoginView(Role.COURIER)));

        int[] ordersOfCourierWithDifferentStatuses = orderService.getOrdersOfCourierWithDifferentStatuses(courier.getId());

        DefaultPieDataset dataset = new DefaultPieDataset();

        String[] statusTypes = {"Чака куриер", "В процес на доставка", "Доставена"};
        for (int i = 0; i < ordersOfCourierWithDifferentStatuses.length; i++)
        {
            if (ordersOfCourierWithDifferentStatuses[i] > 0)
            {
                dataset.setValue(statusTypes[i] + " [" + ordersOfCourierWithDifferentStatuses[i] + "]", ordersOfCourierWithDifferentStatuses[i]);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Графика за брой поръчки със статус “Чака куриер”,  “В процес на доставка” и “Доставена”",
                dataset,
                true,
                true,
                false);

        PiePlot plot = (PiePlot) chart.getPlot();

        Color[] colors = {Color.RED,Color.BLUE,Color.GREEN};

        for (int i = 0; i < statusTypes.length; i++)
        {
            if (ordersOfCourierWithDifferentStatuses[i] > 0)
            {
                plot.setSectionPaint(statusTypes[i], colors[i]);
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
