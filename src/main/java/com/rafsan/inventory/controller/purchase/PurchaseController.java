package com.rafsan.inventory.controller.purchase;

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
import com.rafsan.inventory.interfaces.PurchaseInterface;
import com.rafsan.inventory.entity.Purchase;
import com.rafsan.inventory.model.PurchaseModel;
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

public class PurchaseController implements Initializable, PurchaseInterface {    
    @FXML
    private TableView<Purchase> purchaseTable;
    @FXML
    private TableColumn<Purchase, Long> idColumn;
    @FXML
    private TableColumn<Purchase, String> productColumn, supplierColumn, dateColumn,codigoColumn,costoColumn;
    @FXML
    private TableColumn<Purchase, Double> quantityColumn, priceColumn, totalColumn;
    @FXML
    private TextField searchField; 
    @FXML
    private DatePicker fecIni, fecFin; // AGREGAMOS LOS DATOS DEL CAMPO DE FECHAS
    @FXML
    private Button menu;
    @FXML
    private VBox drawer;
    private PurchaseModel model;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new PurchaseModel();
        
        drawerAction();
        loadData();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        codigoColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(String.valueOf(p.getValue().getProduct().getCodigo_barra())));
       
        productColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(p.getValue().getProduct().getProductName()));
        
        costoColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(String.valueOf(p.getValue().getProduct().getCosto_adqui())));
        
        supplierColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p)
                -> new SimpleStringProperty(p.getValue().getSupplier().getName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        purchaseTable.setItems(PURCHASELIST);
        
        filterData();
        
    }
    
    private void filterData() {
        FilteredList<Purchase> searchedData = new FilteredList<>(PURCHASELIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(purchase -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (purchase.getProduct().getProductName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (purchase.getProduct().getCategory().getType().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    
                    return false;
                });
            });

            SortedList<Purchase> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(purchaseTable.comparatorProperty());
            purchaseTable.setItems(sortedData);
        });
    }
    
    private void loadData() {
        if(!PURCHASELIST.isEmpty()){
            PURCHASELIST.clear();
        }
        
        PURCHASELIST.addAll(model.getPurchases());
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

        windows("/fxml/Product.fxml", "Product", event);
    }
    
    @FXML
    public void categoryAction(ActionEvent event) throws Exception {

        windows("/fxml/Category.fxml", "Category", event);
    }

    @FXML
    public void salesAction(ActionEvent event) throws Exception {

        windows("/fxml/Sales.fxml", "Sales", event);
    }
    
    @FXML
    public void reportAction(ActionEvent event) throws Exception {
        
        windows("/fxml/Report.fxml", "Factura", event);
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
        stage.setTitle("Inventario :: Versi??n 1.0");
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
    public void addAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/purchase/Add.fxml"));
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
        stage.setTitle("Nueva compra");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    
    /* MOVIMIENTOS PARA GENERAR REPORTE POR INTERVALO DE FECHAS */       
    @FXML
    public void btnFiltro(ActionEvent event) throws Exception {
        buscarFecha();
    }// btnFiltro --> onAction
    
    public void buscarFecha() throws ParseException {
        model = new PurchaseModel();
        
        drawerAction();
        loadDataFecha();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        codigoColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(String.valueOf(p.getValue().getProduct().getCodigo_barra())));
       
        productColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(p.getValue().getProduct().getProductName()));
        
        costoColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p) -> 
                new SimpleStringProperty(String.valueOf(p.getValue().getProduct().getCosto_adqui())));
        
        supplierColumn.setCellValueFactory((TableColumn.CellDataFeatures<Purchase, String> p)
                -> new SimpleStringProperty(p.getValue().getSupplier().getName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        purchaseTable.setItems(PURCHASELIST);
        
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
            alert.setTitle("Campos inv??lidos");
            alert.setHeaderText("Corrija los campos inv??lidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }//validateInput
    
    private void loadDataFecha() throws ParseException {
        if(validateInput()){
            LocalDate date1 = fecIni.getValue();
            LocalDate date2 = fecFin.getValue();

             if(!PURCHASELIST.isEmpty()){
                 PURCHASELIST.clear();
             }

            PURCHASELIST.addAll(model.getPurchaseDate(date1.toString(),date2.toString()));
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
    
   
    public void generateReport(String date1, String date2) throws ParseException, IOException {
        try {
            Document document = new Document(PageSize.A4.rotate(), 35, 30, 50, 50);
            File ruta2 = new File(System.getProperty("user.home") + "\\Documents\\Reportes POS\\compras");
            ruta2.mkdirs(); // crear carpeta en el directorio
            FileOutputStream fs = new FileOutputStream(ruta2+"/Reporte de compras.pdf");// convierte el archivo para generar pdf           
            PdfWriter writer = PdfWriter.getInstance(document, fs);
            document.open();// ABRIR DOCUMENTO

            Paragraph paragraph = new Paragraph("Reporte de compras realizadas");
            document.add(paragraph);
            addEmptyLine(paragraph, 5);
            document.add(Chunk.NEWLINE);// agregar salto de linea
            
            PdfPTable table = createTable(date1,date2);// crear tabla de contenidos
            document.add(table);

            document.close();// CERRAR DOCUMENTO
            
            File ruta_archivo = new File(ruta2 +"/Reporte de compras.pdf");
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
        table.setWidths(new float[]{ 10,10,20,10,20,10,10,10,10});// DEFINIR TAMA??O DE LAS CELDAS
        PdfPCell c1 = new PdfPCell(new Phrase("ID"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("CODIGO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("PRODUCTO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("COSTO ADQUI"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("PROVEEDOR"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CANTIDAD"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("PRECIO VENTA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("TOTAL"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("FECHA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        table.setHeaderRows(1);

        List compraList = new ArrayList();
        compraList.addAll(model.getPurchaseDate(date1,date2));
        List<Purchase> lista = compraList;

        for (Iterator<Purchase> it = lista.iterator(); it.hasNext();) {
            Purchase compraCab = it.next();
            long id = compraCab.getId();
            long cod = compraCab.getProduct().getCodigo_barra();
            String prod = compraCab.getProduct().getDescription();
            double cost = compraCab.getProduct().getCosto_adqui();
            String prov =compraCab.getSupplier().getName();
            double cant = compraCab.getQuantity();
            double prec = compraCab.getPrice();
            double tot = compraCab.getTotal();
            String fec = compraCab.getDate();
            System.out.println("imprimiendo datos extraidos: "+String.valueOf(id)+"--"+String.valueOf(cod)+"--"+String.valueOf(prod)+"--"+String.valueOf(cost)+"--"+prov+"--"+String.valueOf(cant)+"--"+String.valueOf(prec)+"--"+String.valueOf(tot)+"--"+fec);

            table.addCell(String.valueOf(id));// id
            table.addCell(String.valueOf(cod));// codigo
            table.addCell(String.valueOf(prod));// producto
            table.addCell(String.valueOf(cost));// costo adqui
            table.addCell(prov);// proveedor 
            table.addCell(String.valueOf(cant));// cantidad
            table.addCell(String.valueOf(prec));// precio venta
            table.addCell(String.valueOf(tot));// total
            table.addCell(fec);// fecha
            }// for iterator                 
        return table;
    }// CreateTable

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }// addEmptyLine
    
}// purchaseController 446
