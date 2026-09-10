package Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import Email.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import javafx.scene.control.RadioButton;

public class PropertyController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView Prev1;

    @FXML
    private ImageView Prev2;

    @FXML
    private Button btnBrowse1;

    @FXML
    private Button btnBrowse2;

    @FXML
    private Button btnFetch;

    @FXML
    private Button btnListNow;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnUpdateDetails;

    @FXML
    private ComboBox<String> comboApprovedBy;

    @FXML
    private ComboBox<String> comboListProperty;

    @FXML
    private ToggleGroup grpProperty;

    @FXML
    private ToggleGroup grpType;

    @FXML
    private RadioButton radAgriculture;

    @FXML
    private RadioButton radCommercial;

    @FXML
    private RadioButton radConstructed;

    @FXML
    private RadioButton radPlot;

    @FXML
    private RadioButton radResidential;

    @FXML
    private TextField txtAddr;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtDir;

    @FXML
    private TextField txtFront;

    @FXML
    private TextField txtLeft;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtOtherInfo;

    @FXML
    private TextField txtRear;

    @FXML
    private TextField txtRight;

    @FXML
    private TextField txtSize;

    @FXML
    private TextField txtTotalPrice;

    Connection con;
    File pic1;
    File pic2;

    private boolean validateFields(){

        if(!txtMobile.getText().matches("[0-9]{10}"))
        {
            showWarning("Enter valid 10 digit mobile number.");
            txtMobile.requestFocus();
            return false;
        }

        if(txtMobile.getText().trim().isEmpty()){
            showWarning("Please enter mobile number.");
            txtMobile.requestFocus();
            return false;
        }


        if(txtAddr.getText().trim().isEmpty()){
            showWarning("Please enter location/address.");
            txtAddr.requestFocus();
            return false;
        }


        if(txtArea.getText().trim().isEmpty()){
            showWarning("Please enter area.");
            txtArea.requestFocus();
            return false;
        }


        if(txtCity.getText().trim().isEmpty()){
            showWarning("Please enter city.");
            txtCity.requestFocus();
            return false;
        }


        if(txtSize.getText().trim().isEmpty()){
            showWarning("Please enter property size.");
            txtSize.requestFocus();
            return false;
        }


        if(txtTotalPrice.getText().trim().isEmpty()){
            showWarning("Please enter total price.");
            txtTotalPrice.requestFocus();
            return false;
        }


        if(grpType.getSelectedToggle()==null){
            showWarning("Please select property type.");
            return false;
        }


        if(grpProperty.getSelectedToggle()==null){
            showWarning("Please select property category.");
            return false;
        }


        if(comboApprovedBy.getSelectionModel().getSelectedItem()==null){
            showWarning("Please select approved by.");
            return false;
        }


        if(pic1==null){
            showWarning("Please select property front image.");
            return false;
        }


        return true;
    }

    private boolean validateMobileForFetch() {
        if (txtMobile.getText().trim().isEmpty()) {
            showWarning("Please enter mobile number.");
            txtMobile.requestFocus();
            return false;
        }

        if (!txtMobile.getText().matches("[0-9]{10}")) {
            showWarning("Enter valid 10 digit mobile number.");
            txtMobile.requestFocus();
            return false;
        }

        return true;
    }

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

    @FXML
    void doBrowse1(ActionEvent event) {

        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Property Front View");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png", "*.bmp", "*.jpeg")
            );

            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            pic1 = chooser.showOpenDialog(null);

            if (pic1 != null) Prev1.setImage(new Image(new FileInputStream(pic1)));
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doBrowse2(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Property Additional View");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png", "*.bmp", "*.jpeg")
            );

            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            pic2 = chooser.showOpenDialog(null);

            if (pic2 != null) Prev2.setImage(new Image(new FileInputStream(pic2)));
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    void doClear() {
        txtAddr.clear();
        txtArea.clear();
        txtCity.clear();
        grpProperty.selectToggle(null);
        grpType.selectToggle(null);
        txtSize.clear();
        txtFront.clear();
        txtRear.clear();
        txtLeft.clear();
        txtRight.clear();
        txtDir.clear();
        comboApprovedBy.getSelectionModel().clearSelection();
        txtTotalPrice.clear();
        txtOtherInfo.clear();
        pic1 = null;
        pic2 = null;
        Prev1.setImage(null);
        Prev2.setImage(null);
    }

    @FXML
    void doFetch(ActionEvent event) {

        if (!validateMobileForFetch()) return;

        try {
            PreparedStatement pst = con.prepareStatement("select rid, area from properties where mobile = ? and status = 'Available'");
            pst.setString(1, txtMobile.getText());

            ResultSet result = pst.executeQuery();

            comboListProperty.getItems().clear();

            while (result.next()) {
                int id = result.getInt("rid");
                String ar = result.getString("area");

                comboListProperty.getItems().add(id + "-" + ar);
            }

            if (!comboListProperty.getItems().isEmpty()) comboListProperty.getSelectionModel().selectFirst();
            else showWarning("No Property Found.");

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doListDetails(ActionEvent event) {
        if (!validateMobileForFetch()) return;

        try {
            String selected = comboListProperty.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showWarning("Please Select a Property");
                return;
            }

            int rid = Integer.parseInt(selected.split("-")[0]);

            PreparedStatement pst = con.prepareStatement("select * from properties where rid = ?");
            pst.setInt(1,rid);

            ResultSet result = pst.executeQuery();

            if (result.next()) {
                txtAddr.setText(result.getString("location"));
                txtArea.setText(result.getString("area"));
                txtCity.setText(result.getString("city"));

                txtSize.setText(String.valueOf(result.getFloat("size")));
                txtFront.setText(String.valueOf(result.getFloat("front")));
                txtRear.setText(String.valueOf(result.getFloat("rear")));
                txtLeft.setText(String.valueOf(result.getFloat("lft")));
                txtRight.setText(String.valueOf(result.getFloat("rght")));

                txtDir.setText(result.getString("facing"));
                txtTotalPrice.setText(result.getString("price"));
                txtOtherInfo.setText(result.getString("otherinfo"));

                comboApprovedBy.getSelectionModel().select(result.getString("approvedby"));

                String type = result.getString("proptype");
                if(type.equalsIgnoreCase("Commercial")) radCommercial.setSelected(true);
                else if (type.equalsIgnoreCase("Residential")) radResidential.setSelected(true);
                else radAgriculture.setSelected(true);

                String cons = result.getString("constype");
                if (cons.equalsIgnoreCase("Plot")) radPlot.setSelected(true);
                else radConstructed.setSelected(true);

                InputStream pic1 = result.getBinaryStream("pic1");
                if (pic1 != null) Prev1.setImage(new Image(pic1));
                else Prev1.setImage(null);

                InputStream pic2 = result.getBinaryStream("pic2");
                if (pic2 != null) Prev2.setImage(new Image(pic2));
                else Prev2.setImage(null);
            }

            else showWarning("Property Not Found");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doListNow(ActionEvent event) {

        if (!validateFields()) return;;

        String customerName = "";
        String customerEmail = "";

        try {

            PreparedStatement customerPst = con.prepareStatement("SELECT cname, email FROM customers WHERE mobile = ?");

            customerPst.setString(1, txtMobile.getText().trim());

            ResultSet customerResult = customerPst.executeQuery();

            if (customerResult.next()) {
                customerName = customerResult.getString("cname");
                customerEmail = customerResult.getString("email");
            }

            customerResult.close();
            customerPst.close();

            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                showError("Property was NOT saved.\n\n" + "Customer email address was not found.");
                return;
            }

            con.setAutoCommit(false);


            PreparedStatement pst = con.prepareStatement("insert into properties " + "(mobile, location, area, city, size, front, rear, lft, rght, facing, proptype, constype, approvedby, price, otherinfo, pic1, pic2, status)" + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1,txtMobile.getText());
            pst.setString(2, txtAddr.getText());
            pst.setString(3, txtArea.getText());
            pst.setString(4, txtCity.getText());
            pst.setFloat(5, Float.parseFloat(txtSize.getText()));
            pst.setFloat(6, Float.parseFloat(txtFront.getText()));
            pst.setFloat(7, Float.parseFloat(txtRear.getText()));
            pst.setFloat(8, Float.parseFloat(txtLeft.getText()));
            pst.setFloat(9, Float.parseFloat(txtRight.getText()));
            pst.setString(10, txtDir.getText());
            pst.setString(11, ((RadioButton)grpType.getSelectedToggle()).getText());
            pst.setString(12, ((RadioButton)grpProperty.getSelectedToggle()).getText());
            pst.setString(13, comboApprovedBy.getSelectionModel().getSelectedItem());
            pst.setString(14, txtTotalPrice.getText());

            String otherInfo = txtOtherInfo.getText().trim();
            if (otherInfo.isEmpty()) otherInfo = "N/A";
            pst.setString(15, otherInfo);

            FileInputStream stream = new FileInputStream(pic1);
            pst.setBinaryStream(16, (InputStream) stream, (int) pic1.length());

            if (pic2 != null) {
                FileInputStream strm = new FileInputStream(pic2);
                pst.setBinaryStream(17, strm, (int) pic2.length());
            }
            else pst.setNull(17, java.sql.Types.BLOB);

            pst.setString(18, "Available");

            int result = pst.executeUpdate();

            if (result <= 0) {
                con.rollback();
                showError("Property could not be saved.");
                return;
            }

            boolean emailSent = EmailService.sendPropertyListingEmail(
                    customerEmail,
                    customerName,
                    txtAddr.getText().trim(),
                    txtArea.getText().trim(),
                    txtCity.getText().trim(),
                    txtSize.getText().trim(),
                    ((RadioButton) grpType.getSelectedToggle()).getText(),
                    ((RadioButton) grpProperty.getSelectedToggle()).getText(),
                    txtFront.getText().trim(),
                    txtRear.getText().trim(),
                    txtLeft.getText().trim(),
                    txtRight.getText().trim(),
                    txtDir.getText().trim(),
                    txtTotalPrice.getText().trim()
            );

            if (!emailSent) {

                con.rollback();

                showError(
                        "Property was NOT saved.\n\n"
                                + "Confirmation email could not be sent."
                );

                return;
            }

            con.commit();

            showSuccess("Property listed successfully!\n\n" + "Confirmation email sent to:\n" + customerEmail);

            doClear();

        }
        catch (Exception exp) {

            try {
                con.rollback();
            }
            catch (Exception ignored) {
            }

            exp.printStackTrace();

            showError("Property was NOT saved.\n\n" + exp.getMessage());

        }
        finally {

            try {
                con.setAutoCommit(true);
            }
            catch (Exception ignored) {
            }
        }
    }

    @FXML
    void doRemove(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Remove Property?");
        alert.setContentText("Are you sure you want to delete this listing?");

        if(alert.showAndWait().get() != ButtonType.OK) return;

        try {

            String selected = comboListProperty.getSelectionModel().getSelectedItem();

            if (selected == null || selected.trim().isEmpty()) {
                showWarning("Please select a property to remove.");
                return;
            }

            int rid = Integer.parseInt(selected.split("-")[0]);

            PreparedStatement fetchPst = con.prepareStatement("select p.location, p.area, p.city, p.size, p.proptype, p.price, p.mobile, c.cname, c.email from properties p left join customers c on p.mobile = c.mobile where p.rid = ?");

            fetchPst.setInt(1, rid);

            ResultSet result = fetchPst.executeQuery();

            if(!result.next()) {
                result.close();
                fetchPst.close();

                showWarning("Property not found.");
                return;
            }

            String address = result.getString("location");

            String area = result.getString("area");

            String city = result.getString("city");

            String size = String.valueOf(result.getFloat("size"));

            String propertyType = result.getString("proptype");

            String price = result.getString("price");

            String customerName = result.getString("cname");

            String customerEmail = result.getString("email");

            result.close();
            fetchPst.close();

            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                showError("Property was NOT removed.\n\n" + "Customer email address was not found.");
                return;
            }

            con.setAutoCommit(false);

            PreparedStatement pst = con.prepareStatement("delete from properties where rid = ?");

            pst.setInt(1, rid);

            int count = pst.executeUpdate();

            pst.close();

            if (count <= 0) {
                con.rollback();
                showError("Property could not be removed.");
                return;
            }

            boolean emailSent = EmailService.sendPropertyStatusEmail(customerEmail, customerName, address, area, city, size, propertyType, price, "REMOVED");

            if (!emailSent) {
                con.rollback();
                showError("Property was NOT removed.\n\n" + "Removal confirmation email could not be sent.");
                return;
            }

            con.commit();

            comboListProperty.getItems().remove(selected);

            doClear();

            showSuccess("Property removed successfully! ✅\n\n" + "Removal confirmation email sent to:\n" + customerEmail);

        }
        catch (Exception exp) {
            try {
                con.rollback();
            }
            catch (Exception ignored) {
            }

            exp.printStackTrace();

            showError("Property removal failed.\n\n" + exp.getMessage());

        }
        finally {

            try {
                con.setAutoCommit(true);
            }
            catch (Exception ignored) {
            }
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {

        if (!validateFields()) return;

        String customerName = "";
        String customerEmail = "";

        try {

            PreparedStatement customerPst = con.prepareStatement("SELECT cname, email FROM customers WHERE mobile = ?");

            customerPst.setString(1, txtMobile.getText().trim());

            ResultSet customerResult = customerPst.executeQuery();

            if (customerResult.next()) {
                customerName = customerResult.getString("cname");
                customerEmail = customerResult.getString("email");
            }

            customerResult.close();
            customerPst.close();

            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                showError("Property was NOT saved.\n\n" + "Customer email address was not found.");
                return;
            }

            String selected = comboListProperty.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showWarning("Please select a property.");
                return;
            }

            int rid = Integer.parseInt(selected.split("-")[0]);

            PreparedStatement pst = con.prepareStatement("update properties set constype=?, price=?, otherinfo=?, pic1=?, pic2=? where rid=?");

            pst.setString(1,((RadioButton) grpProperty.getSelectedToggle()).getText());

            pst.setString(2, txtTotalPrice.getText());

            String otherInfo = txtOtherInfo.getText().trim();
            if (otherInfo.isEmpty())
                otherInfo = "N/A";

            pst.setString(3, otherInfo);

            if (pic1 != null) {
                FileInputStream stream = new FileInputStream(pic1);
                pst.setBinaryStream(4, stream, (int) pic1.length());
            } else {
                pst.setNull(4, java.sql.Types.BLOB);
            }

            if (pic2 != null) {
                FileInputStream stream = new FileInputStream(pic2);
                pst.setBinaryStream(5, stream, (int) pic2.length());
            } else {
                pst.setNull(5, java.sql.Types.BLOB);
            }

            pst.setInt(6, rid);

            int count = pst.executeUpdate();

            if (count > 0) {

                boolean emailSent = EmailService.sendPropertyStatusEmail(
                        customerEmail,
                        customerName,
                        txtAddr.getText(),
                        txtArea.getText(),
                        txtCity.getText(),
                        txtSize.getText(),
                        ((RadioButton) grpType.getSelectedToggle()).getText(),
                        txtTotalPrice.getText(),
                        "UPDATED"
                );

                if (emailSent) showSuccess("✅ Property details updated successfully.\n\n" + "Confirmation email sent to:\n" + customerEmail);
                else showWarning("Property details were updated successfully,\n" + "but the confirmation email could not be sent.");
            }

            else {
                showWarning("❌ Property not found.");
            }

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void initialize() {
        doConnect();
        String approvedBy[] = {"RERA Approved", "Municipal Corporation", "Municipal Council", "Development Authority", "Gram Panchayat", "Improvement Trust", "Private Colony"};
        comboApprovedBy.getItems().addAll(approvedBy);
        comboApprovedBy.getSelectionModel().select("Select");
    }

    public void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if(con == null) showError("Connection Error");
        else System.out.println("All is Well");
    }

}
