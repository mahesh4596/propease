package Deals;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Email.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jdbcc.DatabaseConnection;

public class DealsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCancelDeal;

    @FXML
    private Button btnFetchB;

    @FXML
    private Button btnFetchP;

    @FXML
    private Button btnFinalizeDeal;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> comboP;

    @FXML
    private DatePicker dealDate;

    @FXML
    private DatePicker regDate;

    @FXML
    private TextField txtAdvPaid;

    @FXML
    private TextField txtBAddr;

    @FXML
    private TextField txtBMobile;

    @FXML
    private TextField txtBName;

    @FXML
    private TextField txtBalAmt;

    @FXML
    private TextField txtComm;

    @FXML
    private TextField txtDealStatus;

    @FXML
    private TextField txtFinalDealPrice;

    @FXML
    private TextField txtPAddr;

    @FXML
    private TextField txtPMobile;

    @FXML
    private TextField txtPOwner;

    @FXML
    private TextField txtPStatus;

    @FXML
    private ComboBox<String> comboStatus;

    Connection con;

    private boolean validateFields() {

        if(!txtBMobile.getText().matches("[0-9]{10}"))
        {
            showWarning("Enter valid 10 digit buyer mobile number.");
            txtBMobile.requestFocus();
            return false;
        }

        if (comboP.getValue() == null) {
            showWarning("Please select a property.");
            return false;
        }

        if (txtBMobile.getText().trim().isEmpty()) {
            showWarning("Buyer mobile number is required.");
            txtBMobile.requestFocus();
            return false;
        }

        if (txtFinalDealPrice.getText().trim().isEmpty()) {
            showWarning("Enter final deal price.");
            txtFinalDealPrice.requestFocus();
            return false;
        }

        if (txtAdvPaid.getText().trim().isEmpty()) {
            showWarning("Enter advance amount.");
            txtAdvPaid.requestFocus();
            return false;
        }

        if (dealDate.getValue() == null) {
            showWarning("Select deal date.");
            return false;
        }

        if (regDate.getValue() == null) {
            showWarning("Select registry date.");
            return false;
        }

        try {

            Double.parseDouble(txtFinalDealPrice.getText());
            Double.parseDouble(txtAdvPaid.getText());

            if(!txtComm.getText().trim().isEmpty())
                Double.parseDouble(txtComm.getText());

        }
        catch(Exception e){

            showWarning("Price, Advance and Commission must be numbers only.");
            return false;
        }

        return true;
    }

    private void showSuccess(String msg){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Success");

        alert.setHeaderText("Operation Completed");

        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void showWarning(String msg){

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Validation");

        alert.setHeaderText("Missing Information");

        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void showError(String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");

        alert.setHeaderText("Database Error");

        alert.setContentText(msg);

        alert.showAndWait();
    }

    private boolean confirmCancel(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Cancel Deal");

        alert.setHeaderText("Cancel Current Deal");

        alert.setContentText("Are you sure you want to cancel this deal?");

        return alert.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK;
    }

    @FXML
    void doCancelDeal(ActionEvent event) {

        if(!confirmCancel()) return;

        try {

            String selected = comboP.getValue();

            if(selected==null) {
                showWarning("Please select property first.");
                return;
            }

            int rid = Integer.parseInt(selected.split(" - ")[0]);

            String buyerName = "";
            String buyerEmail = "";

            String sellerName = "";
            String sellerEmail = "";

            String propertyAddress = "";

            String dealPrice = "";
            String dealDate = "";
            String registryDate = "";

            PreparedStatement infoPst = con.prepareStatement("SELECT d.buyerMobile, d.sellerMobile, d.dealPrice, d.dealDate, d.registryDate, cb.cname as buyerName, cb.email as buyerEmail, cs.cname as sellerName, cs.email as sellerEmail, p.location from deals d join properties p on d.rid = p.rid join customers cb on d.buyerMobile = cb.mobile join customers cs on d.sellerMobile = cs.mobile where d.rid = ?");

            infoPst.setInt(1, rid);

            ResultSet result = infoPst.executeQuery();

            if (!result.next()) {
                result.close();
                infoPst.close();
                showWarning("No deal found for this property.");
                return;
            }

            buyerName = result.getString("buyerName");
            buyerEmail = result.getString("buyerEmail");

            sellerName = result.getString("sellerName");
            sellerEmail = result.getString("sellerEmail");

            propertyAddress = result.getString("location");

            dealPrice = result.getString("dealPrice");

            dealDate = result.getString("dealDate");
            registryDate = result.getString("registryDate");

            result.close();
            infoPst.close();

            if (buyerEmail == null || buyerEmail.trim().isEmpty()) {
                showWarning("Deal was not cancelled.\n\n" + "Buyer email address was not found.");
                return;
            }

            if (sellerEmail == null || sellerEmail.trim().isEmpty()) {
                showWarning("Deal was not cancelled.\n\n" + "Seller email address was not found.");
                return;
            }

            PreparedStatement pst = con.prepareStatement("DELETE FROM deals WHERE rid = ?");

            pst.setInt(1, rid);

            int count = pst.executeUpdate();

            if (count > 0) {

                PreparedStatement pst2 = con.prepareStatement("UPDATE properties SET status = ? WHERE rid = ?");

                pst2.setString(1, "Available");
                pst2.setInt(2, rid);

                pst2.executeUpdate();

                boolean buyerEmailSent = EmailService.sendDealStatusEmail(buyerEmail, buyerName, sellerName, propertyAddress, dealPrice, dealDate, registryDate, "Cancelled", true);

                boolean sellerEmailSent = EmailService.sendDealStatusEmail(sellerEmail, sellerName, buyerName, propertyAddress, dealPrice, dealDate, registryDate, "Cancelled", false);

                if (buyerEmailSent && sellerEmailSent)
                    showSuccess("Deal cancelled successfully.\n" + "Property is available again.\n\n" + "Cancellation email sent to Buyer and Seller.");

                else if (buyerEmailSent)
                    showWarning("Deal cancelled successfully.\n" + "Property is available again.\n\n" + "Buyer cancellation email sent successfully.\n" + "Seller email could not be sent.");

                else if (sellerEmailSent)
                    showWarning("Deal cancelled successfully.\n" + "Property is available again.\n\n" + "Seller cancellation email sent successfully.\n" + "Buyer email could not be sent.");

                else
                    showWarning("Deal cancelled successfully.\n" + "Property is available again.\n\n" + "Cancellation emails could not be sent.");

//                doClear(null);

            }
            else showWarning("No deal found for this property.");

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }

    }

    @FXML
    void doFetchB(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("select cname, address, city from customers where mobile = ?");
            pst.setString(1, txtBMobile.getText());

            ResultSet result = pst.executeQuery();
            if (result.next()) {
                txtBName.setText(result.getString("cname"));

                String fullAddr = result.getString("address") + " " + result.getString("city");
                txtBAddr.setText(fullAddr);

                showSuccess("Buyer details loaded successfully.");
            }

            else {
                txtBName.clear();;
                txtBAddr.clear();

                showWarning("Buyer not found.");
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doFetctchP(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("select p.location, p.mobile, p.status, c.cname from properties p join customers c on p.mobile = c.mobile where p.rid = ?");

            String selected = comboP.getValue();
            int rid = Integer.parseInt(selected.split(" - ")[0]);
            pst.setInt(1, rid);

            ResultSet result = pst.executeQuery();

            if (result.next()) {
                txtPAddr.setText(result.getString("location"));
                txtPMobile.setText(result.getString("mobile"));
                txtPStatus.setText(result.getString("status"));
                txtPOwner.setText(result.getString("cname"));

                showSuccess("Property details loaded successfully.");
            }
            else showWarning("Property not found.");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doFinalizeDeal(ActionEvent event) {

        if (!validateFields()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Finalize Deal");
        alert.setHeaderText("Confirm Property Deal");
        alert.setContentText("Are you sure you want to finalize this deal?");

        if(alert.showAndWait().get()!=ButtonType.OK) return;

        try {
            PreparedStatement pst = con.prepareStatement("insert into deals(rid, sellerMobile, buyerMobile, dealPrice, advanceAmount, balanceAmount, commission, dealDate, registryDate, dealStatus) values (?,?,?,?,?,?,?,?,?,?)");

            String selected = comboP.getValue();
            int rid = Integer.parseInt(selected.split(" - ")[0]);
            pst.setInt(1, rid);

            pst.setString(2, txtPMobile.getText());
            pst.setString(3, txtBMobile.getText());
            pst.setString(4, txtFinalDealPrice.getText());
            pst.setString(5,txtAdvPaid.getText());
            pst.setString(6, txtBalAmt.getText());
            pst.setString(7, txtComm.getText());

            LocalDate local = dealDate.getValue();
            java.sql.Date ddate = java.sql.Date.valueOf(local);
            pst.setDate(8, ddate);

            LocalDate local2 = regDate.getValue();
            java.sql.Date rdate = java.sql.Date.valueOf(local2);
            pst.setDate(9, rdate);

            pst.setString(10, (String) comboStatus.getSelectionModel().getSelectedItem());

            int count = pst.executeUpdate();

            if (count <= 0) {
                showError("Deal could not be finalized.");
                return;
            }

            PreparedStatement pst2 = con.prepareStatement("update properties set status = ? where rid = ?");
            pst2.setString(1, "Sold");

            pst2.setInt(2, rid);
            pst2.executeUpdate();

//            showSuccess("🎉 Deal finalized successfully.\nProperty marked as SOLD.");
//            doClear(null);

            String buyerName = "";
            String buyerEmail = "";

            PreparedStatement buyerPst = con.prepareStatement("SELECT cname, email FROM customers WHERE mobile = ?");

            buyerPst.setString(1, txtBMobile.getText().trim());

            ResultSet buyerResult = buyerPst.executeQuery();

            if (buyerResult.next()) {
                buyerName = buyerResult.getString("cname");
                buyerEmail = buyerResult.getString("email");
            }

            buyerResult.close();
            buyerPst.close();

            String sellerName = "";
            String sellerEmail = "";

            PreparedStatement sellerPst = con.prepareStatement("SELECT cname, email FROM customers WHERE mobile = ?");

            sellerPst.setString(1, txtPMobile.getText().trim());

            ResultSet sellerResult = sellerPst.executeQuery();

            if (sellerResult.next()) {
                sellerName = sellerResult.getString("cname");
                sellerEmail = sellerResult.getString("email");
            }

            sellerResult.close();
            sellerPst.close();

            if (buyerEmail == null || buyerEmail.trim().isEmpty()) {
                showWarning("Deal finalized successfully.\n\n" + "However, buyer email was not found.");
                return;
            }


            boolean buyerEmailSent = EmailService.sendDealConfirmationEmail(
                    buyerEmail,
                    buyerName,
                    sellerName,
                    txtPAddr.getText().trim(),
                    txtPMobile.getText().trim(),
                    txtFinalDealPrice.getText().trim(),
                    txtAdvPaid.getText().trim(),
                    txtBalAmt.getText().trim(),
                    txtComm.getText().trim(),
                    dealDate.getValue().toString(),
                    regDate.getValue().toString(),
                    comboStatus.getValue()
            );

            boolean sellerEmailSent = EmailService.sendSellerDealConfirmationEmail(
                    sellerEmail,
                    sellerName,
                    buyerName,
                    txtPAddr.getText().trim(),
                    txtBMobile.getText().trim(),
                    txtFinalDealPrice.getText().trim(),
                    txtAdvPaid.getText().trim(),
                    txtBalAmt.getText().trim(),
                    txtComm.getText().trim(),
                    dealDate.getValue().toString(),
                    regDate.getValue().toString(),
                    comboStatus.getValue()
            );

            if (buyerEmailSent && sellerEmailSent) showSuccess("🎉 Deal finalized successfully!\n\n" + "Property has been marked as SOLD.\n\n" + "Confirmation emails sent to:\n" + "Buyer: " + buyerEmail + "\n" + "Seller: " + sellerEmail);

            else if (buyerEmailSent) showWarning("Deal finalized successfully.\n\n" + "Buyer email sent successfully.\n" + "Seller email could not be sent.");

            else if (sellerEmailSent) showWarning("Deal finalized successfully.\n\n" + "Seller email sent successfully.\n" + "Buyer email could not be sent.");

            else showWarning("Deal finalized successfully.\n\n" + "Neither buyer nor seller confirmation email could be sent.");

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {

        if (!validateFields()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Update Deal");
        alert.setHeaderText("Confirm Update");
        alert.setContentText("Update deal details?");

        if(alert.showAndWait().get()!=ButtonType.OK) return;

        try {
            String selected = comboP.getValue();

            if (selected == null || selected.trim().isEmpty()) {
                showWarning("Please select a property.");
                return;
            }

            int rid = Integer.parseInt(selected.split(" - ")[0]);

            String buyerName = "";
            String buyerEmail = "";

            String sellerName = "";
            String sellerEmail = "";

            String propertyAddress = "";

            PreparedStatement infoPst = con.prepareStatement("select d.buyerMobile, d.sellerMobile, cb.cname as buyerName, cb.email as buyerEmail, cs.cname as sellerName, cs.email as sellerEmail, p.location from deals d join properties p on d.rid = p.rid join customers cb on d.buyerMobile = cb.mobile join customers cs on d.sellerMobile = cs.mobile where d.rid = ?");

            infoPst.setInt(1, rid);

            ResultSet result = infoPst.executeQuery();

            if (!result.next()) {
                result.close();
                infoPst.close();
                showWarning("Deal not found.");
                return;
            }

            buyerName = result.getString("buyerName");
            buyerEmail = result.getString("buyerEmail");

            sellerName = result.getString("sellerName");
            sellerEmail = result.getString("sellerEmail");

            propertyAddress = result.getString("location");

            result.close();
            infoPst.close();

            if (buyerEmail == null || buyerEmail.trim().isEmpty()) {
                showWarning("Deal was not updated.\n\n" + "Buyer email address was not found.");
                return;
            }

            if (sellerEmail == null || sellerEmail.trim().isEmpty()) {
                showWarning("Deal was not updated.\n\n" + "Seller email address was not found.");
                return;
            }

            PreparedStatement pst = con.prepareStatement("update deals set dealPrice = ?, advanceAmount = ?, balanceAmount = ?, commission = ?, dealDate = ?, registryDate = ?, dealStatus = ? where rid = ?");

            pst.setString(1, txtFinalDealPrice.getText());
            pst.setString(2,txtAdvPaid.getText());
            pst.setString(3, txtBalAmt.getText());
            pst.setString(4, txtComm.getText());

            LocalDate local = dealDate.getValue();
            java.sql.Date ddate = java.sql.Date.valueOf(local);
            pst.setDate(5, ddate);

            LocalDate local2 = regDate.getValue();
            java.sql.Date rdate = java.sql.Date.valueOf(local2);
            pst.setDate(6, rdate);

            pst.setString(7, (String) comboStatus.getSelectionModel().getSelectedItem());

            pst.setInt(8, rid);

            int count = pst.executeUpdate();

            if (count > 0) {
                PreparedStatement pst2 = con.prepareStatement("update properties set status = ? where rid = ?");

                if (comboStatus.getValue().equalsIgnoreCase("Completed"))
                    pst2.setString(1, "Sold");

                else if (comboStatus.getValue().equalsIgnoreCase("Cancelled"))
                    pst2.setString(1, "Available");

                else
                    pst2.setString(1, "Booked");

                pst2.setInt(2, rid);
                pst2.executeUpdate();
                pst2.close();

                boolean buyerEmailSent = EmailService.sendDealStatusEmail(buyerEmail, buyerName, sellerName, propertyAddress, txtFinalDealPrice.getText().trim(), local.toString(), local2.toString(), "Updated", true);

                boolean sellerEmailSent = EmailService.sendDealStatusEmail(sellerEmail, sellerName, buyerName, propertyAddress, txtFinalDealPrice.getText().trim(), local.toString(), local2.toString(), "Updated", false);

                if (buyerEmailSent && sellerEmailSent)
                    showSuccess("Deal details updated successfully.\n\n" + "Confirmation email sent to Buyer and Seller.");

                else if (buyerEmailSent)
                    showWarning("Deal details updated successfully.\n\n" + "Buyer email sent successfully.\n" + "Seller email could not be sent.");

                else if (sellerEmailSent)
                    showWarning("Deal details updated successfully.\n\n" + "Seller email sent successfully.\n" + "Buyer email could not be sent.");

                else
                    showWarning("Deal details updated successfully.\n\n" + "Emails could not be sent to Buyer or Seller.");

//                doClear(null);
            }
            else showWarning("Unable to update deal.");
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    private void loadPropertyId() {
        try {
            PreparedStatement pst = con.prepareStatement("select rid, area from properties where status = 'Available'");
            ResultSet result = pst.executeQuery();

            while (result.next()) {
                int id = result.getInt("rid");
                String ar = result.getString("area");
                comboP.getItems().add(id + " - " + ar);
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

        loadPropertyId();

        String types[] = {"Pending", "Advanced Paid", "Registry Pending", "Completed", "Cancelled"};
        comboStatus.getItems().addAll(types);
        comboStatus.getSelectionModel().select("Select");
    }

    void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if (con == null) showError("Connection Error");
        else System.out.println("All is Well!!!!");
    }

}
