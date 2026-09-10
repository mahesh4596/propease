package com.example.javaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Customerv/CustomerView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Propertyv/PropertyView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Dealsv/DealsView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/BrowseCustomers/BrowseCustomersView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/PropertyFinderv/PropertyFinderView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/DealsFinderv/DealsFinderView.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Chartv/ChartView.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Loginv/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 690);
        stage.setTitle("PropEase");
        stage.setScene(scene);
        stage.show();
    }
}
