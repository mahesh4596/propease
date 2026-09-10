package Customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import Email.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import javafx.scene.control.Alert;

public class CustomerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView Prev1;

    @FXML
    private ImageView Prev2;

    @FXML
    private Button btnBrowseAdhaar;

    @FXML
    private Button btnBrowsePic;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField txtAddr;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtMail;

    @FXML
    private TextField txtMob;

    @FXML
    private TextField txtName;

    Connection con;

    File fileProfile;
    File fileAdhaar;

    private void showSuccess(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showError(String msg) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showWarning(String msg) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Warning");
        alert.setHeaderText("Validation");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private boolean validateFields() {

        if (txtMob.getText().trim().isEmpty()) {
            showWarning("Mobile Number is required.");
            txtMob.requestFocus();
            return false;
        }

        if (!txtMob.getText().matches("\\d{10}")) {
            showWarning("Enter a valid 10-digit Mobile Number.");
            txtMob.requestFocus();
            return false;
        }

        if (txtName.getText().trim().isEmpty()) {
            showWarning("Customer Name is required.");
            txtName.requestFocus();
            return false;
        }

        if (txtAddr.getText().trim().isEmpty()) {
            showWarning("Address is required.");
            txtAddr.requestFocus();
            return false;
        }

        if (txtCity.getText().trim().isEmpty()) {
            showWarning("City is required.");
            txtCity.requestFocus();
            return false;
        }

        if (txtMail.getText().trim().isEmpty()) {
            showWarning("Email is required.");
            txtMail.requestFocus();
            return false;
        }

        if (!txtMail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showWarning("Invalid Email Address.");
            txtMail.requestFocus();
            return false;
        }

        if (comboType.getSelectionModel().getSelectedIndex() == 0) {
            showWarning("Please select Customer Type.");
            comboType.requestFocus();
            return false;
        }

        if (fileProfile == null) {
            showWarning("Please select Customer Photo.");
            return false;
        }

        if (fileAdhaar == null) {
            showWarning("Please select Aadhaar Card.");
            return false;
        }

        return true;
    }

    @FXML
    void doBrowseAdhaarCard(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Select Aadhaar Card Image");

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png","*.bmp", "*.jpeg")
            );

            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFile = chooser.showOpenDialog(btnBrowseAdhaar.getScene().getWindow());

            if (selectedFile == null)
                return;

            fileAdhaar = selectedFile;

            Prev2.setImage(new Image(new FileInputStream(fileAdhaar)));

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to load Aadhaar image.\n\n" + exp.getMessage());
        }
    }

    @FXML
    void doBrowseCustomerPic(ActionEvent event) throws FileNotFoundException {

        try {
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Select Customer Photo");

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png", "*.bmp", "*.jpeg")
            );

            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFile = chooser.showOpenDialog(btnBrowsePic.getScene().getWindow());

            if (selectedFile == null)
                return;

            fileProfile = selectedFile;

            Prev1.setImage(new Image(new FileInputStream(fileProfile)));

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to load customer photo.\n\n" + exp.getMessage());
        }
    }

    @FXML
    void doClear(ActionEvent event) {
        txtMob.clear();
        txtName.clear();
        txtAddr.clear();
        txtCity.clear();
        txtMail.clear();
        comboType.getSelectionModel().clearSelection();
        Prev1.setImage(null);
        Prev2.setImage(null);
    }

    @FXML
    void doDelete(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("delete from customers where mobile = ?");
            pst.setString(1, txtMob.getText());

            int count = pst.executeUpdate();

            if (count > 0) {
                doClear(event);
                showSuccess("Record Deleted Successfully");
            }
            else showWarning("No Record Found");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doSave(ActionEvent event) {

        if (!validateFields()) return;

        try {
            PreparedStatement pst = con.prepareStatement("insert into customers values(?,?,?,?,?,?,?,?,current_date())");
            pst.setString(1,txtMob.getText());
            pst.setString(2,txtName.getText());
            pst.setString(3,txtAddr.getText());
            pst.setString(4, txtCity.getText());
            pst.setString(5, txtMail.getText());
            pst.setString(6, comboType.getSelectionModel().getSelectedItem());

            FileInputStream strm = new FileInputStream(fileProfile);

            pst.setBinaryStream(7, (InputStream) strm, (int) fileProfile.length());

            FileInputStream stream = new FileInputStream(fileAdhaar);

            pst.setBinaryStream(8, (InputStream) stream, (int) fileAdhaar.length());

            int result = pst.executeUpdate();

            if(result > 0) {

                boolean emailSent =
                        EmailService.sendCustomerWelcomeEmail(
                                txtMail.getText().trim(),
                                txtName.getText().trim(),
                                txtMob.getText().trim(),
                                txtCity.getText().trim(),
                                comboType.getSelectionModel().getSelectedItem()
                        );

                if(emailSent)
                    showSuccess("Customer added successfully.\n\n" + "Confirmation email sent to:\n" + txtMail.getText());

                else {
                    showSuccess("Customer added successfully.");
                    showWarning("Customer was saved, but " + "confirmation email could not be sent.");
                }
            }

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doSearch(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("select * from customers where mobile = ?");
            pst.setString(1, txtMob.getText());

            ResultSet result = pst.executeQuery();
            if (result.next()) {
                String name = result.getString("cname");
                String add = result.getString("address");
                String city = result.getString("city");
                String email = result.getString("email");
                String ct = result.getString("ctype");

                txtName.setText(name);
                txtAddr.setText(add);
                txtCity.setText(city);
                txtMail.setText(email);
                comboType.getSelectionModel().select(ct);

                InputStream pic = result.getBinaryStream("pic");
                Prev1.setImage(new Image(pic));

                InputStream adhaar = result.getBinaryStream("acard");
                Prev2.setImage(new Image(adhaar));
            }
            else showWarning("Invalid Mobile Number");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {

        if (!validateFields()) return;

        try {
            PreparedStatement pst = con.prepareStatement("update customers set cname = ?, address = ?, city = ?, email = ?, ctype = ?, pic = ?, acard = ? where mobile = ?");

            pst.setString(1, txtName.getText());
            pst.setString(2, txtAddr.getText());
            pst.setString(3, txtCity.getText());
            pst.setString(4, txtMail.getText());
            pst.setString(5, comboType.getSelectionModel().getSelectedItem());

            File pic = new File(fileProfile.getAbsolutePath());
            FileInputStream strm = new FileInputStream(pic);
            pst.setBinaryStream(6, (InputStream) strm, (int) pic.length());

            File adhaar = new File(fileAdhaar.getAbsolutePath());
            FileInputStream stream = new FileInputStream(adhaar);
            pst.setBinaryStream(7, (InputStream) stream, (int) adhaar.length());

            pst.setString(8, txtMob.getText());

            int count = pst.executeUpdate();

            if(count > 0) {

                boolean emailSent = EmailService.sendCustomerUpdateEmail(
                        txtMail.getText(),
                        txtName.getText(),
                        txtMob.getText(),
                        txtCity.getText(),
                        comboType.getSelectionModel().getSelectedItem()
                );

                if(emailSent) showSuccess("Customer Updated successfully.\n\n" + "Confirmation email sent to:\n" + txtMail.getText());

                else {
                    showSuccess("Customer updated successfully! ✅");
                    showWarning("Customer was updated, but the " + "confirmation email could not be sent.");
                }

                doClear(event);
            }
            else showWarning("No Customer Record Found");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void initialize() {
        doConnect();
        String types[] = {"Seller", "Buyer", "Both Seller and Buyer"};
        comboType.getItems().addAll(types);
        comboType.getSelectionModel().select("Select");
    }

    public void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if(con == null) showError("Connection Error");
        else System.out.println("All is Well");
    }

}
