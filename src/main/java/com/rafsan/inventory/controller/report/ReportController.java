package com.rafsan.inventory.controller.report;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.interfaces.ReportInterface;
import com.rafsan.inventory.model.InvoiceModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ReportController implements Initializable, ReportInterface {

    @FXML
    private TableView<Invoice> reportTable;
    @FXML
    private TableColumn<Invoice, Long> idColumn;
    @FXML
    private TableColumn<Invoice, String> employeeColumn, dateColumn;
    @FXML
    private TableColumn<Invoice, Double> totalColumn, vatColumn, discountColumn, 
            payableColumn, paidColumn, returnedColumn;
    @FXML
    private DatePicker fecIni, fecFin; // AGREGAMOS LOS DATOS DEL CAMPO DE FECHAS
    @FXML
    private TextField searchField;
    @FXML
    private Button viewButton;
    private InvoiceModel model;

    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Button menu;
    @FXML
    private VBox drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new InvoiceModel();
        
        drawerAction();
        loadData();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColumn.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> p)
                -> new SimpleStringProperty(p.getValue().getEmployee().getUserName()+": "+p.getValue().getEmployee().getFirstName()+" "+p.getValue().getEmployee().getLastName()));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        payableColumn.setCellValueFactory(new PropertyValueFactory<>("payable"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
        returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reportTable.setItems(REPORTLIST);

        filterData();

        viewButton
                .disableProperty()
                .bind(Bindings.isEmpty(reportTable.getSelectionModel().getSelectedItems()));
    }

    private void filterData() {
        FilteredList<Invoice> searchedData = new FilteredList<>(REPORTLIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(report -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return report.getEmployee().getUserName().toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<Invoice> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(reportTable.comparatorProperty());
            reportTable.setItems(sortedData);
        });
    }
    
    private void loadData(){
    
        if (!REPORTLIST.isEmpty()) {
            REPORTLIST.clear();
        }
        REPORTLIST.addAll(model.getInvoices());
    }
    
    private void drawerAction() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        menu.setOnAction((ActionEvent evt) -> {
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                menu.getStyleClass().remove("hamburger-button");
                menu.getStyleClass().add("open-menu");
            } else {
                closeNav.setToX(-(drawer.getWidth()));
                closeNav.play();
                menu.getStyleClass().remove("open-menu");
                menu.getStyleClass().add("hamburger-button");
            }
        });
    }

    @FXML
    public void adminAction(ActionEvent event) throws Exception {
        windows("/fxml/Admin.fxml", "Admin", event);
    }
    
    @FXML
    public void productAction(ActionEvent event) throws Exception {

        windows("/fxml/Admin.fxml", "Admin", event);
    }
    
    @FXML
    public void categoryAction(ActionEvent event) throws Exception {

        windows("/fxml/Category.fxml", "Category", event);
    }

    @FXML
    public void purchaseAction(ActionEvent event) throws Exception {

        windows("/fxml/Purchase.fxml", "Purchase", event);
    }

    @FXML
    public void salesAction(ActionEvent event) throws Exception {

        windows("/fxml/Sales.fxml", "Sales", event);
    }

    @FXML
    public void supplierAction(ActionEvent event) throws Exception {
        windows("/fxml/Supplier.fxml", "Supplier", event);
    }
    
    @FXML
    public void staffAction(ActionEvent event) throws Exception {
        windows("/fxml/Employee.fxml", "Employee", event);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.setTitle("Inventario :: Versión 1.0");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    private void windows(String path, String title, ActionEvent event) throws Exception {

        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void viewAction(ActionEvent event) throws IOException {

        Invoice selectedInvoice = reportTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/fxml/report/View.fxml")));
        ViewController controller = new ViewController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("View Details");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        controller.setReport(selectedInvoice);
        reportTable.getSelectionModel().clearSelection();
    }
    
        /* MOVIMIENTOS PARA GENERAR REPORTE POR INTERVALO DE FECHAS */
        
    @FXML
    public void btnFiltro(ActionEvent event) throws Exception {
        buscarFecha();
    }// btnFiltro --> onAction
    
    public void buscarFecha() throws ParseException {
        model = new InvoiceModel();
        
        drawerAction();
        loadDataFecha();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColumn.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> p)
                -> new SimpleStringProperty(p.getValue().getEmployee().getUserName()+": "+p.getValue().getEmployee().getFirstName()+" "+p.getValue().getEmployee().getLastName()));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        payableColumn.setCellValueFactory(new PropertyValueFactory<>("payable"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
        returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reportTable.setItems(REPORTLIST);
        
        filterData();     
    }//buscarFechas
    
    private boolean validateInput() {
        String errorMessage = "";

        if (fecIni.getValue() == null) {
            errorMessage += "Ingrese fecha inicial !!\n";
        }

        if (fecFin.getValue() == null) {
            errorMessage += "Ingrese fecha final !!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos inválidos");
            alert.setHeaderText("Corrija los campos inválidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }//validateInput

    private void loadDataFecha() throws ParseException {
        if(validateInput()){
            LocalDate date1 = fecIni.getValue();
            LocalDate date2 = fecFin.getValue();

             if(!REPORTLIST.isEmpty()){
                 REPORTLIST.clear();
             }

             REPORTLIST.addAll(model.getInvoicesDate(date1.toString(),date2.toString()));
        }// loadDataFecha
    }

    /* MOVIMIENTOS PARA IMPRIMIR REPORTE PDF POR INTERVALO DE FECHAS */  
    @FXML
    public void btnImprimir(ActionEvent event) throws Exception {
        if(validateInput()){
            LocalDate date1 = fecIni.getValue();
            LocalDate date2 = fecFin.getValue();
            generateReport(date1.toString(),date2.toString());// mandar a generar el reporte
        }// validateInput
    }// btnFiltro --> onAction
    
   
    public void generateReport(String date1, String date2) throws ParseException, FileNotFoundException, DocumentException {
        try {
            Document document = new Document(PageSize.A4.rotate(), 35, 30, 50, 50);
            File ruta2 = new File(System.getProperty("user.home") + "\\Documents\\Reportes POS\\facturas");
            ruta2.mkdirs(); // crear carpeta en el directorio
            FileOutputStream fs = new FileOutputStream(ruta2+"/Reporte de facturas.pdf");// convierte el archivo para generar pdf    
            PdfWriter writer = PdfWriter.getInstance(document, fs);
            document.open();// ABRIR DOCUMENTO

            Paragraph paragraph = new Paragraph("Reporte de facturas realizadas");
            document.add(paragraph);
            addEmptyLine(paragraph, 5);
            document.add(Chunk.NEWLINE);// agregar salto de linea
            
            PdfPTable table = createTable(date1,date2);// crear tabla de contenidos
            document.add(table);

            document.close();// CERRAR DOCUMENTO
            
            File ruta_archivo = new File(ruta2 +"/Reporte de facturas.pdf");
            System.out.println("ruta: " + ruta_archivo);
            try {
                System.out.println("ABRIENDO RUTAS");
                Desktop.getDesktop().open(ruta_archivo); // aqui termina
            } catch (IOException ex) {}
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }// generarReport

    private PdfPTable createTable(String date1, String date2) throws ParseException, DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);// EVALUAR EL 100 DEL MARGEN
        table.setWidths(new float[]{ 15,20,10,10,10,10,10,10,10});// DEFINIR TAMAÑO DE LAS CELDAS
        
        PdfPCell c1 = new PdfPCell(new Phrase("ID"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("USUARIO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("TOTAL"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("IVA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("DESC."));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("NETO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("RECIBIDO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CAMBIO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("FECHA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        table.setHeaderRows(1);

        List compraList = new ArrayList();
        compraList.addAll(model.getInvoicesDate(date1,date2));
        List<Invoice> lista = compraList;
        
        for (Iterator<Invoice> it = lista.iterator(); it.hasNext();) {
            Invoice compraCab = it.next();
            String id = compraCab.getId();
            String user = compraCab.getEmployee().getUserName()+": "+compraCab.getEmployee().getFirstName()+" "+compraCab.getEmployee().getLastName();
            double tot = compraCab.getTotal();
            double iva = compraCab.getVat();
            double desc =compraCab.getDiscount();
            double neto = compraCab.getPayable();
            double reci = compraCab.getPaid();
            double camb = compraCab.getReturned();
            String fec = compraCab.getDate();
            System.out.println("imprimiendo datos extraidos: "+id+"--"+user+"--"+String.valueOf(tot)+"--"+String.valueOf(iva)+"--"+String.valueOf(desc)+"--"+String.valueOf(neto)+"--"+String.valueOf(reci)+"--"+String.valueOf(camb)+"--"+fec);

            table.addCell(String.valueOf(id));// id
            table.addCell(user);// user
            table.addCell(String.valueOf(tot));// total
            table.addCell(String.valueOf(iva));// iva
            table.addCell(String.valueOf(desc));// descuento
            table.addCell(String.valueOf(neto));// neto
            table.addCell(String.valueOf(reci));// recibido
            table.addCell(String.valueOf(camb));// cambio
            table.addCell(fec);// fecha
            }// for iterator                 
        return table;
    }// CreateTable

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }// addEmptyLine
}//reportController 441
