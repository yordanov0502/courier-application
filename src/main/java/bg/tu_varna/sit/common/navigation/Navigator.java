package bg.tu_varna.sit.common.navigation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Navigator extends MouseAdapter {

    private View view1;
    private View view2;

    public Navigator(View view1,View view2) {
        this.view1 = view1;
        this.view2 = view2;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        view1.close();
        view2.open();
    }
}
