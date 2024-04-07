package bg.tu_varna.sit.application;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.presentation.WelcomeView;

public class Main{
    public static void main(String[] args) {
        Connection.createSessionFactory();
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.open();
    }

}