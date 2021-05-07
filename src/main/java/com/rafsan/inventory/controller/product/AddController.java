package com.rafsan.inventory.controller.product;

import com.rafsan.inventory.interfaces.ProductInterface;
import com.rafsan.inventory.entity.Category;
import com.rafsan.inventory.entity.Product;
import com.rafsan.inventory.entity.Supplier;
import com.rafsan.inventory.model.CategoryModel;
import com.rafsan.inventory.model.ProductModel;
import com.rafsan.inventory.model.SupplierModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController implements Initializable, ProductInterface {

    @FXML
    private TextField nameField, priceField, quantityField, codBarra, costoAdqui;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox categoryBox, supplierBox;
    @FXML
    private Button saveButton;
    private ProductModel productModel;
    private CategoryModel categoryModel;
    private SupplierModel supplierModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productModel = new ProductModel();
        categoryModel = new CategoryModel();
        supplierModel = new SupplierModel();
        ObservableList<String> categoryList = FXCollections.observableArrayList(categoryModel.getTypes());
        ObservableList<String> supplierList = FXCollections.observableArrayList(supplierModel.getNames());
        categoryBox.setItems(categoryList);
        supplierBox.setItems(supplierList);
    }

    @FXML
    public void handleSave(ActionEvent event) {

        if (validateInput()) {

            Category category = categoryModel.getCategory(categoryBox.getSelectionModel().getSelectedIndex() + 1);
            Supplier supplier = supplierModel.getSupplier(supplierBox.getSelectionModel().getSelectedIndex() + 1);
            Product product = new Product(
                    nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Double.parseDouble(quantityField.getText()),
                    descriptionArea.getText(),category,
                    supplier,
                    Integer.parseInt(codBarra.getText()),
                    Double.parseDouble(costoAdqui.getText())
            );

            productModel.saveProduct(product);
            PRODUCTLIST.clear();
            PRODUCTLIST.addAll(productModel.getProducts());

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exitoso");
            alert.setHeaderText("Producto agregado");
            alert.setContentText("El producto se agrega con éxito");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        descriptionArea.setText("");
        codBarra.setText("");
        costoAdqui.setText("");
        categoryBox.valueProperty().setValue(null);
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Sin nombre válido!\n";
        }

        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "Sin precio válido!\n";
        }

        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "Sin cantidad válida!\n";
        }

        if (descriptionArea.getText() == null || descriptionArea.getText().length() == 0) {
            errorMessage += "Sin descripción de correo electrónico!\n";
        }

        if (codBarra.getText() == null || codBarra.getText().length() == 0) {
            errorMessage += "Sin codigo de barra!\n";
        }
        
        if (costoAdqui.getText() == null || costoAdqui.getText().length() == 0) {
            errorMessage += "Sin costo de adquisicion!\n";
        }
        
        if (categoryBox.getSelectionModel().isEmpty()) {
            errorMessage += "Por favor seleccione la categoría!\n";
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
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
