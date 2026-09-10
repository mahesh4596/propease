package Chart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import jdbcc.DatabaseConnection;
import javafx.scene.control.Alert;

public class ChartConroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PieChart customerType;

    @FXML
    private PieChart dealStatus;

    @FXML
    private PieChart propertiesStatus;

    @FXML
    private PieChart propertiesType;

    Connection con;

    private void showSuccess(String msg){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Success");
        alert.setHeaderText("Dashboard Loaded");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showWarning(String msg){

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("No Data");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showError(String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Dashboard Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void initialize() {
        try {
            con = DatabaseConnection.doConnectDB();

            if (con == null) showError("Unable to connect with database.");

            // ====================== FOR PROPERTY TYPE ====================================

            PreparedStatement pst1 = con.prepareStatement("select proptype,count(*) as count from properties group by proptype");

            ResultSet resultproptype = pst1.executeQuery();

            ObservableList<PieChart.Data> proptype = FXCollections.observableArrayList();

            while (resultproptype.next()) {
                String propType = resultproptype.getString("proptype");
                int count = resultproptype.getInt("count");
//                System.out.println(propType + " " + count);
                proptype.add(new PieChart.Data(propType,count));
            }

            if(proptype.isEmpty()) showWarning("No property type data available.");

            propertiesType.setData(proptype);


            // ====================== FOR DEAL STATUS ====================================

            PreparedStatement pst2 = con.prepareStatement("select dealStatus,count(*) as count from deals group by dealStatus");

            ResultSet resultdealstatus = pst2.executeQuery();

            ObservableList<PieChart.Data> dealstatus = FXCollections.observableArrayList();

            while (resultdealstatus.next()) {
                String status = resultdealstatus.getString("dealStatus");
                int count = resultdealstatus.getInt("count");
//                System.out.println(status + " " + count);
                dealstatus.add(new PieChart.Data(status,count));
            }

            if(dealstatus.isEmpty()) showWarning("No deal status data available.");

            dealStatus.setData(dealstatus);


            // ====================== FOR CUSTOMER TYPE ====================================

            PreparedStatement pst3 = con.prepareStatement("select ctype,count(*) as count from customers group by ctype");

            ResultSet resultcustomertype = pst3.executeQuery();

            ObservableList<PieChart.Data> customertype = FXCollections.observableArrayList();

            while (resultcustomertype.next()) {
                String type = resultcustomertype.getString("ctype");
                int count = resultcustomertype.getInt("count");
//                System.out.println(type + " " + count);
                customertype.add(new PieChart.Data(type,count));
            }

            if(customertype.isEmpty()) showWarning("No customer data available.");

            customerType.setData(customertype);

            // ====================== FOR PROPERTIES STATUS ====================================

            PreparedStatement pst4 = con.prepareStatement("select status,count(*) as count from properties group by status");

            ResultSet resultpropstatus = pst4.executeQuery();

            ObservableList<PieChart.Data> propstatus = FXCollections.observableArrayList();

            while (resultpropstatus.next()) {
                String ptype = resultpropstatus.getString("status");
                int count = resultpropstatus.getInt("count");
//                System.out.println(ptype + " " + count);
                propstatus.add(new PieChart.Data(ptype,count));
            }

            if(propstatus.isEmpty()) showWarning("No property status data available.");
            else showSuccess("Dashboard charts loaded successfully.");

            propertiesStatus.setData(propstatus);
        }
        catch(Exception exp){
            exp.printStackTrace();
            showError("Unable to load dashboard charts.\n" + exp.getMessage());
        }
        finally{
            try{
                if(con != null) con.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
