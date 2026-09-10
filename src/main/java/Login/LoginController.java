package Login;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import Dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jdbcc.DatabaseConnection;

import javafx.scene.control.ButtonType;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView profileImg;

    @FXML
    private TextField txtUsername;

    Connection con;

    private void showSuccess(String msg){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Success");
        alert.setHeaderText("Login Successful");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showWarning(String msg){

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("Login Validation");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showError(String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Login Failed");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private boolean validateLogin(){

        String username = txtUsername.getText().trim();
        String pass = password.getText().trim();


        if(username.isEmpty()){

            showWarning("Please enter username.");
            txtUsername.requestFocus();
            return false;
        }


        if(pass.isEmpty()){

            showWarning("Please enter password.");
            password.requestFocus();
            return false;
        }


        if(username.length() < 3){

            showWarning("Username must contain at least 3 characters.");
            txtUsername.requestFocus();
            return false;
        }


        if(pass.length() < 4){

            showWarning("Password must contain at least 4 characters.");
            password.requestFocus();
            return false;
        }


        return true;
    }


    @FXML
    void doLogin(ActionEvent event) {
        if(!validateLogin())
            return;


        String username = txtUsername.getText().trim();
        String pass = password.getText().trim();

        try {

            if(con == null){
                showError("Database connection not available.");
                return;
            }

            PreparedStatement pst = con.prepareStatement("select * from users where username=? and password=?");

            pst.setString(1, username);
            pst.setString(2, pass);

            ResultSet result = pst.executeQuery();

            if (result.next()) {

                showSuccess("Welcome " + username);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboardv/DashboardView.fxml"));

                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                // stage.setFullScreen(true);
                stage.setScene(scene);
                stage.setTitle("PropEase");
                stage.show();

            }
            else {
                showError("Invalid username or password.");
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to login.\n" + exp.getMessage());
        }
    }

    @FXML
    void initialize() {
        doConnect();
        password.setOnAction(e -> {btnLogin.fire();});
    }

    void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if (con == null) showError("Connection Error");
        else System.out.println("All is Well");
    }

}
