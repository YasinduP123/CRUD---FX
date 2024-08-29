package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.GetCustomers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddCustomerFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> ColDob;

    @FXML
    private JFXComboBox<String> comboTitle;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCity;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPostalCode;

    @FXML
    private TableColumn<?, ?> colProvince;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableView<GetCustomers> custTable;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private DatePicker txtDob;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    GetCustomers getCustomers;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("insert into customer values (?,?,?,?,?,?,?,?,?)");

            stm.setObject(1, txtId.getText());
            stm.setObject(2, comboTitle.getValue());
            stm.setObject(3, txtName.getText());
            stm.setObject(4, txtDob.getValue());
            stm.setObject(5, Double.parseDouble(txtSalary.getText()));
            stm.setObject(6, txtAddress.getText());
            stm.setObject(7, txtCity.getText());
            stm.setObject(8, txtProvince.getText());
            stm.setObject(9, txtPostalCode.getText());

            int rst = stm.executeUpdate();

            System.out.println(rst > 0 ? "Customer Added Success " + rst + " rows affected..." : "Customer Didn't added Success...");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Cannot enter null values... ");
            System.out.println(e);
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("Delete from customer where CustId = ?");
            stm.setObject(1,txtId.getText());
            int rst = stm.executeUpdate();

            System.out.println(rst>0 ? "Customer details delete success "+rst+" rows affected" : "Customer details didn't delete... ");

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }catch (NullPointerException e){
            System.out.println("Eka Awlak naa...");
        }

    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        load();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("update customer set CustTitle=? ,CustName=? , DOB=?,  Salary=? , CustAddress=? , City=? , Province=? , PostalCode=? where CustId=?");
            stm.setObject(1, comboTitle.getValue());
            stm.setObject(2, txtName.getText());
            stm.setObject(3, txtDob.getValue());
            stm.setObject(4, Double.parseDouble(txtSalary.getText()));
            stm.setObject(5, txtAddress.getText());
            stm.setObject(6, txtCity.getText());
            stm.setObject(7, txtProvince.getText());
            stm.setObject(8, txtPostalCode.getText());

            stm.setObject(9,txtId.getText());

            int rst = stm.executeUpdate();

            System.out.println(rst>0 ? "Customer details update success "+rst+" rows affected" : "Customer details didn't update... ");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> titleList = FXCollections.observableArrayList();
        titleList.add("Mr.");
        titleList.add("Miss.");
        titleList.add("Ms.");

        comboTitle.setItems(titleList);
        load();
        try {
            custTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
                addValueToText(newVal);
            });
        }catch (NullPointerException e){
            System.out.println("Eka Awlak Naa...");
        }

    }

    private void load() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("select * from customer");
            ResultSet rst = stm.executeQuery();

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            ColDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
            colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

            ObservableList<GetCustomers> observableList = FXCollections.observableArrayList();


            while (rst.next()) {

                getCustomers = new GetCustomers(
                        rst.getString("CustID"),
                        rst.getString("CustTitle") + rst.getString("CustName"),
                        rst.getString("DOB"),
                        rst.getDouble("Salary"),
                        rst.getString("CustAddress"),
                        rst.getString("City"),
                        rst.getString("Province"),
                        rst.getString("PostalCode")
                );
                observableList.add(getCustomers);
            }

            custTable.setItems(observableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (NullPointerException e){
            System.out.println(e);
            System.out.println("There are not title to Customer...");
        }
    }

//    private void loadUpdate() {
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            PreparedStatement stm = connection.prepareStatement("select * from customer");
//            ResultSet rst = stm.executeQuery();
//
//
//            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
//            ColDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
//            colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
//            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
//            colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
//            colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
//            colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
//
//            ObservableList<GetCustomers> observableList = FXCollections.observableArrayList();
//
//
//            while (rst.next()) {
//
//                getCustomers = new GetCustomers(
//                        rst.getString("CustTitle") + rst.getString("CustName"),
//                        rst.getString("DOB"),
//                        rst.getDouble("Salary"),
//                        rst.getString("CustAddress"),
//                        rst.getString("City"),
//                        rst.getString("Province"),
//                        rst.getString("PostalCode")
//                );
//                observableList.add(getCustomers);
//            }
//
//            custTable.setItems(observableList);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }catch (NullPointerException e){
//            System.out.println(e);
//            System.out.println("did not have title to Customer...");
//        }
//    }

    private void addValueToText(GetCustomers newVal) {

        try {

            txtId.setText(newVal.getId());
            txtName.setText(newVal.getName());
            txtDob.setValue(LocalDate.parse(newVal.getDob()));
            txtSalary.setText("" + newVal.getSalary());
            txtAddress.setText(newVal.getAddress());
            txtCity.setText(newVal.getCity());
            txtProvince.setText(newVal.getProvince());
            txtPostalCode.setText(newVal.getPostalCode());
        }catch (NullPointerException e){
            System.out.println("Eka Awlak naa...");
        }

    }

}



