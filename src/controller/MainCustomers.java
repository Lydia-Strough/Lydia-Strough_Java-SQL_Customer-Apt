package controller;

import DAO.CustomerDao;
import DAO.CustomerDaoImpl;
import DAO.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainCustomers implements Initializable {
    Stage stage;
    Parent scene;
    public TextField searchCustomer;
    public TableView<Customer> customerTableView;
    public TableColumn customerIdCol;
    public TableColumn customerNameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneNumCol;
    public TableColumn stateProvinceCol;
    public Label userTimeZoneLbl;

    public void onActionSearchCustomer(ActionEvent actionEvent) {
        JDBC.openConnection();
        CustomerDao customerDao = new CustomerDaoImpl();
        customerTableView.setItems(customerDao.getAllCustomers());

        try{
            int customerId = Integer.parseInt(searchCustomer.getText());
            Customer customer = customerDao.lookUpCustomer(customerId);
            customerTableView.getSelectionModel().select(customer);
            customerTableView.scrollTo(customer);
            customerTableView.requestFocus();
        }catch(NumberFormatException e) {
            String customerName = searchCustomer.getText();
            ObservableList<Customer> customers = customerDao.lookUpCustomer(customerName);
            customerTableView.setItems(customers);
        }
        if(!((CustomerDaoImpl) customerDao).customerFound){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No item was found.");
            alert.showAndWait();
        }
    }

    public void onActionAddCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Customer Button Clicked!");

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionUpdateCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Update Customer Button Clicked!");

        /*
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/UpdateCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

         */

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
        Parent scene = loader.load();

        UpdateCustomer updateCustomerController = loader.getController();

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        updateCustomerController.updateCustomer(selectedCustomer);

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionDeleteCustomer(ActionEvent actionEvent) {
        System.out.println("Delete Customer Button Clicked!");
    }

    public void onActionAppointments(ActionEvent actionEvent) throws IOException {
        System.out.println("Appointments Button Clicked!");

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionReports(ActionEvent actionEvent) throws IOException {
        System.out.println("Reports Button Clicked!");

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportCustomerAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionLogout(ActionEvent actionEvent) {
        System.out.println("Logout Button Clicked!");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you wish to Exit the program?");
        alert.showAndWait();

        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent() && result.get() == ButtonType.OK)) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Customer Records: I am initialized!");

        userTimeZoneLbl.setText("Your Time Zone: " + String.valueOf(ZoneId.systemDefault()));

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateProvinceCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        JDBC.openConnection();
        CustomerDao customerDao = new CustomerDaoImpl();
        customerTableView.setItems(customerDao.getAllCustomers());
    }
}
