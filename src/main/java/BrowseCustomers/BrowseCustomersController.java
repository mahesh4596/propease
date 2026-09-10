package BrowseCustomers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;


public class BrowseCustomersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<CustomersBean> TableCustomers;

    @FXML
    private ComboBox<String> comboCategory;

    Connection con;

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
        alert.setHeaderText("Something went wrong");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void doExportExcel(ActionEvent event) {

        if(TableCustomers.getItems().isEmpty()){
            showWarning("No customer records available to export.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export Excel");
        alert.setHeaderText("Export Customer Data");
        alert.setContentText("Do you want to export customer records?");

        if(alert.showAndWait().get()!=ButtonType.OK) return;

        try {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Customer Data");

            Drawing<?> drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Mobile");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Address");
            header.createCell(3).setCellValue("City");
            header.createCell(4).setCellValue("Email");
            header.createCell(5).setCellValue("Photo");
            header.createCell(6).setCellValue("Adhaar Card");
            header.createCell(7).setCellValue("Date");

            ObservableList<CustomersBean> data = TableCustomers.getItems();

            int rowIndex = 1;

            for (CustomersBean customer: data) {
                Row row = sheet.createRow(rowIndex);

                row.createCell(0).setCellValue(customer.getMobile());
                row.createCell(1).setCellValue(customer.getCname());
                row.createCell(2).setCellValue(customer.getAddress());
                row.createCell(3).setCellValue(customer.getCity());
                row.createCell(4).setCellValue(customer.getEmail());

                row.setHeightInPoints(90);

                if (customer.getPicBytes() != null) {
                    int pictureIndex = workbook.addPicture(
                            customer.getPicBytes(),
                            Workbook.PICTURE_TYPE_JPEG
                    );

                    ClientAnchor anchor = helper.createClientAnchor();

                    anchor.setCol1(5);
                    anchor.setRow1(rowIndex);
                    anchor.setCol2(6);
                    anchor.setRow2(rowIndex + 1);

                    Picture picture = drawing.createPicture(anchor, pictureIndex);

                    picture.resize(1.0);
                }

                if (customer.getAcardBytes() != null) {

                    int pictureIndex = workbook.addPicture(
                            customer.getAcardBytes(),
                            Workbook.PICTURE_TYPE_JPEG
                    );

                    ClientAnchor anchor = helper.createClientAnchor();

                    anchor.setCol1(6);
                    anchor.setRow1(rowIndex);
                    anchor.setCol2(7);
                    anchor.setRow2(rowIndex + 1);

                    Picture picture = drawing.createPicture(anchor, pictureIndex);
                }

                row.createCell(7).setCellValue(customer.getDoe());

                rowIndex++;

            }

            for(int i=0;i<8;i++) sheet.setColumnWidth(i, 20 * 250);

            // Save File
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Save Excel File");
            chooser.setInitialFileName("Customers.xlsx");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files","*.xlsx")
            );

            File file = chooser.showSaveDialog(TableCustomers.getScene().getWindow());

            if(file != null)
            {
                FileOutputStream fos = new FileOutputStream(file);

                workbook.write(fos);

                fos.close();
                workbook.close();

                showSuccess("Customer data exported successfully.");
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doGeneratePdf(ActionEvent event) {

        if(TableCustomers.getItems().isEmpty()) {
            showWarning("No customer records available for PDF.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Generate PDF");
        alert.setHeaderText("Customer Report");
        alert.setContentText("Generate PDF report?");

        if(alert.showAndWait().get()!=ButtonType.OK) return;


        FileChooser chooser = new FileChooser();

        chooser.setTitle("Save PDF");
        chooser.setInitialFileName("Customers.pdf");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = chooser.showSaveDialog(TableCustomers.getScene().getWindow());

        if (file == null)
            return;

        try {

            PdfWriter writer = new PdfWriter(file);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4.rotate());

            document.add(new Paragraph("Customer Details")
                    .setBold()
                    .setFontSize(18));

            float[] widths = {70, 70, 90, 60, 100, 60, 60, 60};

            Table table = new Table(widths);
            table.useAllAvailableWidth();

            table.addHeaderCell("Mobile");
            table.addHeaderCell("Name");
            table.addHeaderCell("Address");
            table.addHeaderCell("City");
            table.addHeaderCell("Email");
            table.addHeaderCell("Photo");
            table.addHeaderCell("Aadhaar");
            table.addHeaderCell("Date");

            ObservableList<CustomersBean> list = TableCustomers.getItems();

            for (CustomersBean customer : list) {

                table.addCell(customer.getMobile());
                table.addCell(customer.getCname());
                table.addCell(customer.getAddress());
                table.addCell(customer.getCity());
                table.addCell(customer.getEmail());

                // Photo
                if (customer.getPicBytes() != null) {

                    ImageData imgData = ImageDataFactory.create(customer.getPicBytes());

                    com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imgData);

                    pdfImage.scaleToFit(50, 50);

                    table.addCell(new Cell().add(pdfImage));

                }
                else table.addCell("No Image");

                // Aadhaar
                if (customer.getAcardBytes() != null) {

                    ImageData imgData = ImageDataFactory.create(customer.getAcardBytes());

                    com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imgData);

                    pdfImage.scaleToFit(50, 50);

                    table.addCell(new Cell().add(pdfImage));

                }
                else table.addCell("No Image");

                table.addCell(customer.getDoe());
            }

            document.add(table);

            document.close();

            showSuccess("PDF Generated Successfully");

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError(exp.getMessage());
        }
    }

    @FXML
    void doShowData(ActionEvent event) {

        TableCustomers.getColumns().clear();

        TableColumn<CustomersBean, String> mobileCol = new TableColumn<>("Mobile");
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobileCol.setMinWidth(125);

        TableColumn<CustomersBean, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("cname"));
        nameCol.setMinWidth(130);

        TableColumn<CustomersBean, String> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addrCol.setMinWidth(125);

        TableColumn<CustomersBean, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityCol.setMinWidth(125);

        TableColumn<CustomersBean, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setMinWidth(125);

        TableColumn<CustomersBean, Image> picCol = new TableColumn<>("Photo");
        picCol.setCellValueFactory(new PropertyValueFactory<>("pic"));
        picCol.setMinWidth(125);

        picCol.setCellFactory(column -> new TableCell<CustomersBean, Image>() {

            private final ImageView imageView = new ImageView(); {
                imageView.setFitWidth(125);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);

                if (empty || image == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        TableColumn<CustomersBean, Image> adhaarCol = new TableColumn<>("Adhaar Card");
        adhaarCol.setCellValueFactory(new PropertyValueFactory<>("acard"));
        adhaarCol.setMinWidth(125);

        adhaarCol.setCellFactory(column -> new TableCell<CustomersBean, Image>() {

            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(125);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);

                if (empty || image == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        TableColumn<CustomersBean, String> doeCol = new TableColumn<>("Date");
        doeCol.setCellValueFactory(new PropertyValueFactory<>("doe"));
        doeCol.setMinWidth(125);

        TableCustomers.getColumns().addAll(mobileCol, nameCol, addrCol, cityCol, emailCol, picCol, adhaarCol, doeCol);

        String category = comboCategory.getValue();
        if(category == null) {
            showWarning("Please select customer category.");
            return;
        }
        TableCustomers.setItems(getAllRecords(category));
    }

    ObservableList<CustomersBean> getAllRecords(String category) {
        ObservableList<CustomersBean> list = FXCollections.observableArrayList();

        try {
            PreparedStatement pst;

            if (category.equals("All")) pst = con.prepareStatement("select * from customers");
            else {
                pst = con.prepareStatement("select * from customers where ctype = ?");
                pst.setString(1, category);
            }

            ResultSet result = pst.executeQuery();

            while (result.next()) {
                String mobile = result.getString("mobile");
                String name = result.getString("cname");
                String addr = result.getString("address");
                String city = result.getString("city");
                String email = result.getString("email");

                byte[] imageBytes = result.getBytes("pic");
                Image pic = null;
                if (imageBytes != null) pic = new Image(new ByteArrayInputStream(imageBytes));

                byte[] adhaarBytes = result.getBytes("acard");
                Image adhaar = null;
                if (adhaarBytes != null) adhaar = new Image(new ByteArrayInputStream(adhaarBytes));

                java.sql.Date doe1 = result.getDate("doe");

                CustomersBean obj = new CustomersBean(mobile,name,addr,city,email,doe1.toString(),pic,adhaar,imageBytes,adhaarBytes);
                list.add(obj);
//                System.out.println(list);
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to load customer records.\n" + exp.getMessage());
        }

        return list;
    }

    @FXML
    void initialize() {
        doConnect();
        String types[] = {"Buyer", "Seller", "Both Seller and Buyer", "All"};
        comboCategory.getItems().addAll(types);
        comboCategory.getSelectionModel().select("Select");
    }

    void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if(con == null) showError("Connection Error");
        else System.out.println("All is Well");
    }

}