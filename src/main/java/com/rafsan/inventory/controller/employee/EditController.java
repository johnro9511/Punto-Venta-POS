package com.rafsan.inventory.controller.employee;

import com.rafsan.inventory.entity.Employee;
import com.rafsan.inventory.interfaces.EmployeeInterface;
import com.rafsan.inventory.model.EmployeeModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

public class EditController implements Initializable, EmployeeInterface {

    @FXML
    private TextField firstField, lastField, usernameField, phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea addressArea;
    @FXML
    private Button saveButton;
    private long selectedEmployeeId;
    private EmployeeModel employeeModel;
    private Employee employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeModel = new EmployeeModel();
        resetValues();
    }

    public void setEmployee(Employee employee, long selectedEmployeeId) {
        this.employee = employee;
        this.selectedEmployeeId = selectedEmployeeId;
        setData();
    }

    @FXML
    public void handleSave(ActionEvent event) {

        if (validateInput()) {

            Employee editedEmployee = new Employee(
                    employee.getId(),
                    firstField.getText(),
                    lastField.getText(),
                    usernameField.getText(),
                    DigestUtils.sha1Hex(passwordField.getText()),
                    phoneField.getText(),
                    addressArea.getText()
            );

            employeeModel.updateEmployee(editedEmployee);
            EMPLOYEELIST.set((int) selectedEmployeeId, editedEmployee);

            ((Stage) saveButton.getScene().getWindow()).close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exitoso");
            alert.setHeaderText("Empleado actualizado!");
            alert.setContentText("El empleado se actualiza con ??xito");
            alert.showAndWait();
        }
    }

    private void setData() {
        firstField.setText(employee.getFirstName());
        lastField.setText(employee.getLastName());
        usernameField.setText(employee.getUserName());
        passwordField.setText(employee.getPassword());
        phoneField.setText(employee.getPhone());
        addressArea.setText(employee.getAddress());
    }

    private void resetValues() {
        firstField.setText("");
        lastField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        addressArea.setText("");
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        resetValues();
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (firstField.getText() == null || firstField.getText().length() == 0) {
            errorMessage += "Sin nombre de pila v??lido!\n";
        }

        if (lastField.getText() == null || lastField.getText().length() == 0) {
            errorMessage += "Sin apellido v??lido!\n";
        }

        if (usernameField.getText() == null || usernameField.getText().length() == 0) {
            errorMessage += "Ning??n nombre de usuario v??lido!\n";
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Sin contrase??a v??lida!\n";
        }

        if (phoneField.getText() == null || phoneField.getText().length() == 0) {
            errorMessage += "No hay un n??mero de tel??fono v??lido!\n";
        }

        if (addressArea.getText() == null || addressArea.getText().length() == 0) {
            errorMessage += "Sin direcci??n de correo electr??nico!\n";
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
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
