module com.example.javaproject {

    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.graphics;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires jakarta.activation;

    exports com.example.javaproject;

    opens com.example.javaproject to javafx.fxml;

    opens Customer to javafx.fxml;
    exports Customer;

    opens Property to javafx.fxml;
    exports Property;

    opens Deals to javafx.fxml;
    exports Deals;

    opens BrowseCustomers to javafx.fxml;
    exports BrowseCustomers;

    opens PropertyFinder to javafx.fxml;
    exports PropertyFinder;

    opens DealsFinder to javafx.fxml;
    exports DealsFinder;

    opens Chart to javafx.fxml;
    exports Chart;

    opens Login to javafx.fxml;
    exports Login;

    opens Dashboard to javafx.fxml;
    exports Dashboard;

    opens Email to javafx.fxml;
    exports Email;
}