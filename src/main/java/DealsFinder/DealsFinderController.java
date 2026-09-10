package DealsFinder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DealsFinderController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private TableView<DealsBean> TableDeals;

    @FXML
    private ToggleGroup grpStatus;

    @FXML
    private RadioButton radioFinalized;

    @FXML
    private RadioButton radioPending;

    Connection con;

    private boolean validateSearch(){

        if(!radioPending.isSelected() &&
                !radioFinalized.isSelected()){

            showWarning("Please select deal status.");
            return false;
        }


        if(dateFrom.getValue()==null){

            showWarning("Please select starting date.");
            return false;
        }


        if(dateTo.getValue()==null){

            showWarning("Please select ending date.");
            return false;
        }


        if(dateFrom.getValue().isAfter(dateTo.getValue())){

            showWarning("From date cannot be after To date.");
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

        alert.setTitle("Warning");
        alert.setHeaderText("Attention Required");
        alert.setContentText(msg);

        alert.showAndWait();
    }


    private void showError(String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void doExportToExcel(ActionEvent event) {

        if(TableDeals.getItems().isEmpty()){
            showWarning("No deals available to export.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Export Excel");
        alert.setHeaderText("Export Deals Report");
        alert.setContentText("Do you want to export deal records?");

        if(alert.showAndWait().get() != ButtonType.OK) return;

        try {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Deals Data");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Deal Id");
            header.createCell(1).setCellValue("Seller");
            header.createCell(2).setCellValue("Seller Contact");
            header.createCell(3).setCellValue("Buyer");
            header.createCell(4).setCellValue("Buyer Conact");
            header.createCell(5).setCellValue("Deal Price");
            header.createCell(6).setCellValue("Advance");
            header.createCell(7).setCellValue("Balance");
            header.createCell(8).setCellValue("Deal Date");
            header.createCell(9).setCellValue("Registry Date");
            header.createCell(10).setCellValue("Status");

            ObservableList<DealsBean> data = TableDeals.getItems();

            int rowIndex = 1;

            for (DealsBean deals: data) {
                Row row = sheet.createRow(rowIndex);

                row.createCell(0).setCellValue(deals.getDealId());
                row.createCell(1).setCellValue(deals.getSeller());
                row.createCell(2).setCellValue(deals.getSellerContact());
                row.createCell(3).setCellValue(deals.getBuyer());
                row.createCell(4).setCellValue(deals.getBuyerContact());
                row.createCell(5).setCellValue(deals.getDealPrice());
                row.createCell(6).setCellValue(deals.getAdvanceAmt());
                row.createCell(7).setCellValue(deals.getBalanceAmt());
                row.createCell(8).setCellValue(deals.getDealDate());
                row.createCell(9).setCellValue(deals.getRegistryDate());
                row.createCell(10).setCellValue(deals.getStatus());

                rowIndex++;

            }

            for(int i=0;i<8;i++) sheet.setColumnWidth(i, 20 * 250);

            // Save File
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Save Excel File");
            chooser.setInitialFileName("Deals.xlsx");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files","*.xlsx")
            );

            File file = chooser.showSaveDialog(TableDeals.getScene().getWindow());

            if(file != null)
            {
                FileOutputStream fos = new FileOutputStream(file);

                workbook.write(fos);

                fos.close();
                workbook.close();

                showSuccess("Excel Exported Successfully");
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doGeneratePDF(ActionEvent event) {

        if(TableDeals.getItems().isEmpty()){
            showWarning("No deals available for PDF.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Generate PDF");
        alert.setHeaderText("Deals Report");
        alert.setContentText("Generate PDF report?");

        if(alert.showAndWait().get() != ButtonType.OK) return;


        FileChooser chooser = new FileChooser();

        chooser.setTitle("Save PDF");
        chooser.setInitialFileName("Deals.pdf");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = chooser.showSaveDialog(TableDeals.getScene().getWindow());

        if (file == null)
            return;

        try {

            PdfWriter writer = new PdfWriter(file);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4.rotate());

            document.add(new Paragraph("Deals Details").setBold().setFontSize(18));

            float[] widths = {60, 100, 90, 100, 90, 100, 80, 80, 80, 90, 70};

            com.itextpdf.layout.element.Table table = new Table(widths);
            table.useAllAvailableWidth();

            table.addHeaderCell("Deal Id");
            table.addHeaderCell("Seller");
            table.addHeaderCell("Seller Contact");
            table.addHeaderCell("Buyer");
            table.addHeaderCell("Buyer Contact");
            table.addHeaderCell("Deal Price");
            table.addHeaderCell("Advance");
            table.addHeaderCell("Balance");
            table.addHeaderCell("Deal Date");
            table.addHeaderCell("Registry Date");
            table.addHeaderCell("Status");

            ObservableList<DealsBean> list = TableDeals.getItems();

            for (DealsBean deals : list) {

                table.addCell(String.valueOf(deals.getDealId()));
                table.addCell(deals.getSeller());
                table.addCell(deals.getSellerContact());
                table.addCell(deals.getBuyer());
                table.addCell(deals.getBuyerContact());
                table.addCell(deals.getDealPrice());
                table.addCell(deals.getAdvanceAmt());
                table.addCell(deals.getBalanceAmt());
                table.addCell(deals.getDealDate());
                table.addCell(deals.getRegistryDate());
                table.addCell(deals.getStatus());
            }

            document.add(table);

            document.close();

            showSuccess("PDF Generated Successfully");

        }
        catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }

    @FXML
    void doSearch(ActionEvent event) {

        if (!validateSearch()) return;

        TableDeals.getColumns().clear();

        TableColumn<DealsBean, String> dealCol = new TableColumn<>("Deal Id");
        dealCol.setCellValueFactory(new PropertyValueFactory<>("dealId"));
        dealCol.setMinWidth(60);

        TableColumn<DealsBean, String> seller = new TableColumn<>("Seller");
        seller.setCellValueFactory(new PropertyValueFactory<>("seller"));
        seller.setMinWidth(110);

        TableColumn<DealsBean, String> sellermob = new TableColumn<>("Seller Contact");
        sellermob.setCellValueFactory(new PropertyValueFactory<>("sellerContact"));
        sellermob.setMinWidth(100);

        TableColumn<DealsBean, String> buyer = new TableColumn<>("Buyer");
        buyer.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        buyer.setMinWidth(110);

        TableColumn<DealsBean, String> buyermob = new TableColumn<>("Buyer Contact");
        buyermob.setCellValueFactory(new PropertyValueFactory<>("buyerContact"));
        buyermob.setMinWidth(100);

        TableColumn<DealsBean, String> dealprice = new TableColumn<>("Deal Price");
        dealprice.setCellValueFactory(new PropertyValueFactory<>("dealPrice"));
        dealprice.setMinWidth(110);

        TableColumn<DealsBean, String> advamt = new TableColumn<>("Advance");
        advamt.setCellValueFactory(new PropertyValueFactory<>("advanceAmt"));
        advamt.setMinWidth(90);

        TableColumn<DealsBean, String> balamt = new TableColumn<>("Balance");
        balamt.setCellValueFactory(new PropertyValueFactory<>("balanceAmt"));
        balamt.setMinWidth(90);

        TableColumn<DealsBean, String> dealdate = new TableColumn<>("Deal Date");
        dealdate.setCellValueFactory(new PropertyValueFactory<>("dealDate"));
        dealdate.setMinWidth(90);

        TableColumn<DealsBean, String> regdate = new TableColumn<>("Registry Date");
        regdate.setCellValueFactory(new PropertyValueFactory<>("registryDate"));
        regdate.setMinWidth(100);

        TableColumn<DealsBean, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setMinWidth(80);

        String statusp = "";
        if (radioPending.isSelected()) statusp = "Pending";
        else if (radioFinalized.isSelected()) statusp = "Completed";
        else {
            showWarning("Please Select deal status");
            return;
        }

        TableDeals.getColumns().addAll(dealCol, seller, sellermob, buyer, buyermob, dealprice, advamt, balamt, dealdate, regdate, status);
        TableDeals.setItems(getAllRecords(statusp, dateFrom.getValue(), dateTo.getValue()));
    }

    ObservableList<DealsBean> getAllRecords(String status, LocalDate fromDate, LocalDate toDate) {
        ObservableList<DealsBean> list = FXCollections.observableArrayList();

        try {
            PreparedStatement pst = con.prepareStatement("select d.dealid, s.cname as seller, d.sellerMobile, b.cname as buyer, d.buyerMobile, d.dealPrice, d.advanceAmount, d.balanceAmount, d.dealDate, d.registryDate, d.dealStatus from deals d join customers s on d.sellerMobile = s.mobile join customers b on d.buyerMobile = b.mobile where d.dealStatus = ? and d.dealDate between ? and ?");

            pst.setString(1, status);

            if(fromDate==null || toDate==null) return list;

            pst.setString(2, String.valueOf(java.sql.Date.valueOf(fromDate)));
            pst.setString(3, String.valueOf(java.sql.Date.valueOf(toDate)));

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                int dealid = result.getInt("dealid");

                String sellername = result.getString("seller");
                String sellermobile = result.getString("sellerMobile");

                String buyername = result.getString("buyer");
                String buyermobile = result.getString("buyerMobile");

                String dealprice = result.getString("dealPrice");
                String advamt = result.getString("advanceAmount");
                String balamt = result.getString("balanceAmount");

                java.sql.Date dealdate = result.getDate("dealDate");
                java.sql.Date regdate = result.getDate("registryDate");

                String dealstatus = result.getString("dealStatus");

                DealsBean obj = new DealsBean(dealid, sellername, sellermobile, buyername, buyermobile, dealprice, advamt, balamt, dealdate.toString(), regdate.toString(), dealstatus);
                list.add(obj);
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to load deals.\n" + exp.getMessage());
        }
        return list;
    }

    @FXML
    void initialize() {
        doConnect();
    }

    void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if(con == null) showError("Connection Error");
        else System.out.println("All is Well");
    }

}