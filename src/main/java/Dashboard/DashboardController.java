package Dashboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DashboardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCharts;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnDeals;

    @FXML
    private Button btnDealsFinder;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnBrowseCustomer;

    @FXML
    private Button btnProperties;

    @FXML
    private Button btnPropertyFinder;

    private void showSuccess(String msg){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showWarning(String msg){

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("Attention");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showError(String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Unable to Open Page");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    void openPage(String path)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            if(loader.getLocation()==null){
                showError("Page not found:\n" + path);
                return;
            }

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setScene(scene);

            // stage.setFullScreen(true);

            stage.setTitle("PropEase");
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
            showError("Cannot open page.\n" + e.getMessage());
        }

    }

    @FXML
    void openCustomer(ActionEvent event) {
        openPage("/Customerv/CustomerView.fxml");
    }

    @FXML
    void openBrowseCustomer(ActionEvent event) {
        openPage("/BrowseCustomers/BrowseCustomersView.fxml");
    }

    @FXML
    void openProperties(ActionEvent event) {
        openPage("/Propertyv/PropertyView.fxml");
    }

    @FXML
    void openPropertyFinder(ActionEvent event) {
        openPage("/PropertyFinderv/PropertyFinderView.fxml");
    }

    @FXML
    void openDeals(ActionEvent event) {
        openPage("/Dealsv/DealsView.fxml");
    }

    @FXML
    void openDealsFinder(ActionEvent event) {
        openPage("/DealsFinderv/DealsFinderView.fxml");
    }

    @FXML
    void openCharts(ActionEvent event) {
        openPage("/Chartv/ChartView.fxml");
    }

    @FXML
    void logout(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        if(alert.showAndWait().get() != ButtonType.OK) return;


        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Loginv/LoginView.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("PropEase");
            stage.show();

            showSuccess("Logged out successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
            showError("Unable to logout.\n" + e.getMessage());
        }
    }

    @FXML
    void initialize() {

        Button buttons[] = {btnCustomer,btnBrowseCustomer,btnProperties,btnPropertyFinder,btnDeals,btnDealsFinder,btnCharts,btnLogout};

        for(Button btn : buttons){
            if(btn != null){
                btn.setOnMouseEntered(e -> btn.setScaleX(1.05));
                btn.setOnMouseExited(e -> {btn.setScaleX(1);});
            }
        }
    }

}
