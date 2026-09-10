package PropertyFinder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import BrowseCustomers.CustomersBean;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
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

public class PropertyFinderController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<PropertyBean> TableProperties;

    @FXML
    private ComboBox<String> comboArea;

    @FXML
    private ComboBox<String> comboCity;

    @FXML
    private ComboBox<String> comboPropType;

    @FXML
    private ComboBox<String> comboStructure;

    @FXML
    private TextField txtMaxPrice;

    @FXML
    private TextField txtMinPrice;

    Connection con;

    List<String> cities = new ArrayList<>();
    List<String> areas = new ArrayList<>();

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

        if(TableProperties.getItems().isEmpty()){
            showWarning("No property records available to export.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Export Excel");
        alert.setHeaderText("Export Property Data");
        alert.setContentText("Do you want to export properties?");

        if(alert.showAndWait().get() != ButtonType.OK) return;

        try {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Customer Data");

            Drawing<?> drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Photo");
            header.createCell(1).setCellValue("Mobile");
            header.createCell(2).setCellValue("Location");
            header.createCell(3).setCellValue("Size");
            header.createCell(4).setCellValue("Facing");
            header.createCell(5).setCellValue("Price");
            header.createCell(6).setCellValue("Approved By");
            header.createCell(7).setCellValue("Status");
            header.createCell(8).setCellValue("Dimensions");

            ObservableList<PropertyBean> data = TableProperties.getItems();

            int rowIndex = 1;

            for (PropertyBean property: data) {
                Row row = sheet.createRow(rowIndex);

                row.setHeightInPoints(90);

                if (property.getImageBytes() != null) {
                    int pictureIndex = workbook.addPicture(
                            property.getImageBytes(),
                            Workbook.PICTURE_TYPE_JPEG
                    );

                    ClientAnchor anchor = helper.createClientAnchor();

                    anchor.setCol1(0);
                    anchor.setRow1(rowIndex);
                    anchor.setCol2(1);
                    anchor.setRow2(rowIndex + 1);

                    Picture picture = drawing.createPicture(anchor, pictureIndex);

                    picture.resize(1.0);
                }

                row.createCell(1).setCellValue(property.getMobile());
                row.createCell(2).setCellValue(property.getLocation());
                row.createCell(3).setCellValue(property.getSize());
                row.createCell(4).setCellValue(property.getFacing());
                row.createCell(5).setCellValue(property.getPrice());
                row.createCell(6).setCellValue(property.getApprovedby());
                row.createCell(7).setCellValue(property.getStatus());
                row.createCell(8).setCellValue(property.getDimensions());

                rowIndex++;

            }

            for(int i=0;i<8;i++) sheet.setColumnWidth(i, 20 * 250);

            // Save File
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Save Excel File");
            chooser.setInitialFileName("Properties.xlsx");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files","*.xlsx")
            );

            File file = chooser.showSaveDialog(TableProperties.getScene().getWindow());

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
    void doFetchArea(ActionEvent event) {
        comboArea.getItems().clear();
        areas.clear();

        try {
            String city = comboCity.getSelectionModel().getSelectedItem();

            if (city == null) {
                showWarning("Please Select City");
                return;
            }

            PreparedStatement pst = con.prepareStatement("select distinct area from properties where city = ?");

            pst.setString(1, city);

            ResultSet result = pst.executeQuery();

            while (result.next()) areas.add(result.getString("area"));

            comboArea.getItems().addAll(areas);
            if (!comboArea.getItems().isEmpty()) comboArea.getSelectionModel().selectFirst();
            else showWarning("No areas found for selected city.");

            result.close();

            pst.close();
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @FXML
    void doGeneratePDF(ActionEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Save PDF");
        chooser.setInitialFileName("Customers.pdf");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = chooser.showSaveDialog(TableProperties.getScene().getWindow());

        if (file == null)
            return;

        try {

            PdfWriter writer = new PdfWriter(file);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4.rotate());

            document.add(new Paragraph("Customer Details").setBold().setFontSize(18));

            float[] widths = {70, 70, 90, 60, 60, 90, 80, 60, 70};

            com.itextpdf.layout.element.Table table = new Table(widths);
            table.useAllAvailableWidth();

            table.addHeaderCell("Photo");
            table.addHeaderCell("Mobile");
            table.addHeaderCell("Location");
            table.addHeaderCell("Size");
            table.addHeaderCell("Facing");
            table.addHeaderCell("Price");
            table.addHeaderCell("Approved By");
            table.addHeaderCell("Status");
            table.addHeaderCell("Dimensions");

            ObservableList<PropertyBean> list = TableProperties.getItems();

            for (PropertyBean property : list) {

                // Photo
                if (property.getImageBytes() != null) {

                    ImageData imgData = ImageDataFactory.create(property.getImageBytes());

                    com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imgData);

                    pdfImage.scaleToFit(50, 50);

                    table.addCell(new Cell().add(pdfImage));

                }
                else table.addCell("No Image");

                table.addCell(property.getMobile());
                table.addCell(property.getLocation());
                table.addCell(property.getSize());
                table.addCell(property.getFacing());
                table.addCell(property.getPrice());
                table.addCell(property.getApprovedby());
                table.addCell(property.getStatus());
                table.addCell(property.getDimensions());
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
    void doSearchProperties(ActionEvent event) {

        if(comboCity.getValue() == null || comboArea.getValue() == null || comboPropType.getValue() == null || comboStructure.getValue() == null) {
            showWarning("Please select all property filters.");
            return;
        }

        if(!txtMinPrice.getText().trim().isEmpty() && !txtMinPrice.getText().matches("[0-9]+")) {
            showWarning("Minimum price must contain numbers only.");
            txtMinPrice.requestFocus();
            return;
        }

        if(!txtMaxPrice.getText().trim().isEmpty() && !txtMaxPrice.getText().matches("[0-9]+")) {
            showWarning("Maximum price must contain numbers only.");
            txtMaxPrice.requestFocus();
            return;
        }

        TableProperties.getColumns().clear();

        TableColumn<PropertyBean, Image> picCol = new TableColumn<>("Photo");
        picCol.setCellValueFactory(new PropertyValueFactory<>("pic1"));
        picCol.setMinWidth(125);

        picCol.setCellFactory(column -> new TableCell<PropertyBean, Image>() {

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

        TableColumn<PropertyBean, String> mobileCol = new TableColumn<>("Mobile");
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobileCol.setMinWidth(125);

        TableColumn<PropertyBean, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        location.setMinWidth(125);

        TableColumn<PropertyBean, String> size = new TableColumn<>("Size");
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        size.setMinWidth(50);

        TableColumn<PropertyBean, String> facing = new TableColumn<>("Facing");
        facing.setCellValueFactory(new PropertyValueFactory<>("facing"));
        facing.setMinWidth(50);

        TableColumn<PropertyBean, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setMinWidth(100);

        TableColumn<PropertyBean, String> approved = new TableColumn<>("Approved By");
        approved.setCellValueFactory(new PropertyValueFactory<>("approvedby"));
        approved.setMinWidth(172);

        TableColumn<PropertyBean, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setMinWidth(90);

        TableColumn<PropertyBean, String> dimensions = new TableColumn<>("Dimensions");
        dimensions.setCellValueFactory(new PropertyValueFactory<>("dimensions"));
        dimensions.setPrefWidth(150);
        dimensions.setCellFactory(column -> new TableCell<PropertyBean, String>() {

            private final Label label = new Label();

            {
                label.setWrapText(true);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setGraphic(null);
                }
                else {
                    label.setText(item);
                    setGraphic(label);
                }
            }
        });

        TableProperties.getColumns().addAll(picCol, mobileCol, location, size, facing, price, approved, status, dimensions);
        TableProperties.setItems(getAllRecords(comboCity.getValue(), comboArea.getValue(), comboPropType.getValue(), comboStructure.getValue(), txtMinPrice.getText(), txtMaxPrice.getText()));

    }

    ObservableList<PropertyBean> getAllRecords(String city, String area, String proptype, String constype, String minprice, String maxprice) {
        ObservableList<PropertyBean> list = FXCollections.observableArrayList();

        try {
            PreparedStatement pst = con.prepareStatement("select * from properties where city = ? and area = ? and proptype = ? and constype = ? and CAST(price AS UNSIGNED) BETWEEN ? AND ?");
            pst.setString(1, city);
            pst.setString(2, area);
            pst.setString(3, proptype);
            pst.setString(4, constype);

            if (minprice == null || minprice.trim().isEmpty()) minprice = "0";

            if (maxprice == null || maxprice.trim().isEmpty()) maxprice = "999999999";

            pst.setInt(5, Integer.parseInt(minprice));
            pst.setInt(6, Integer.parseInt(maxprice));

            ResultSet result = pst.executeQuery();

            while (result.next()) {
                byte[] imageBytes = result.getBytes("pic1");
                Image pic = null;
                if (imageBytes != null) pic = new Image(new ByteArrayInputStream(imageBytes));

                String mobile = result.getString("mobile");
                String loc = result.getString("location");
                String size = result.getString("size");
                String facing = result.getString("facing");
                String pricee = result.getString("price");
                String approved = result.getString("approvedby");
                String status = result.getString("status");

                String front = result.getString("front");
                String rear = result.getString("rear");
                String left = result.getString("lft");
                String right = result.getString("rght");

                String dimensions = "F:" + front + "  R:" + rear + "  L:" + left + "  Rt:" + right;

                PropertyBean obj = new PropertyBean(mobile,loc,area,city,size,front,rear,left,right,facing,proptype,constype,approved,pricee,status,pic,imageBytes,dimensions);
                list.add(obj);
            }

        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to fetch properties.\n"+ exp.getMessage());
        }

        return list;
    }

    void fetchCities() {
        try {
            PreparedStatement pst = con.prepareStatement("select distinct city from properties");

            ResultSet result = pst.executeQuery();

            while (result.next()) cities.add(result.getString("city"));

            pst.close();
        }
        catch (Exception exp) {
            exp.printStackTrace();
            showError("Unable to load cities.");
        }
    }

    @FXML
    void initialize() {
        doConnect();
        fetchCities();
        comboCity.getItems().addAll(cities);
        comboCity.getSelectionModel().select("Select");

        comboArea.getSelectionModel().select("Select");

        String[] propType = {"Residential","Commercial","Agricultural"};
        comboPropType.getItems().addAll(propType);
        comboPropType.getSelectionModel().select("Select");

        String[] structType = {"Plot", "Constructed"};
        comboStructure.getItems().addAll(structType);
        comboStructure.getSelectionModel().select("Select");

    }

    void doConnect() {
        con = DatabaseConnection.doConnectDB();
        if (con == null) showError("Connection Eror");
        else System.out.println("All is Well");
    }

}
