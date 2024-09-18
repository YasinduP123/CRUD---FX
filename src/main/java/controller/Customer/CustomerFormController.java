package controller.Customer;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.util.CrudUtil;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.GetCustomers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

	@FXML
	private TableColumn<?, ?> colDob;

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

	CustomerService service = CustomerController.getInstance();

	@FXML
	void btnAddOnAction(ActionEvent event) {

		Customer customer = new Customer(
				txtId.getText(),
				comboTitle.getValue(),
				txtName.getText(),
				txtDob.getValue(),
				Double.parseDouble(txtSalary.getText()),
				txtAddress.getText(),
				txtCity.getText(),
				txtProvince.getText(),
				txtPostalCode.getText()
		);

		if (service.addCustomer(customer)) {
			new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully...").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Customer did not add success...").show();
		}

		load();

	}

	@FXML
	void btnDeleteOnAction(ActionEvent event) {

		if (service.deleteCustomer(txtId.getText())) {
			new Alert(Alert.AlertType.INFORMATION, "Customer Delete Success...").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Customer Delete not Success !").show();
		}

		load();


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

		Customer customer = new Customer(
				txtId.getText(),
				comboTitle.getValue(),
				txtName.getText(),
				txtDob.getValue(),
				Double.parseDouble(txtSalary.getText()),
				txtAddress.getText(),
				txtCity.getText(),
				txtProvince.getText(),
				txtPostalCode.getText()
		);

		if (service.updateCustomer(customer)) {
			new Alert(Alert.AlertType.INFORMATION, "Customer Update Success...").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Customer Update not Success !").show();
		}

		load();

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
				if (newVal != null) {
					addValueToText(newVal);
				}
			});
		} catch (NullPointerException e) {
			 throw new NullPointerException();
		}

	}

	private void load() {
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement stm = connection.prepareStatement("select * from customer");
			ResultSet rst = stm.executeQuery();

			colId.setCellValueFactory(new PropertyValueFactory<>("id"));
			colName.setCellValueFactory(new PropertyValueFactory<>("name"));
			colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
			colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
			colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
			colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
			colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
			colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

			ObservableList<GetCustomers> observableList = FXCollections.observableArrayList();


			while (rst.next()) {

				GetCustomers getCustomers = new GetCustomers(
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
		}

	}

	private void addValueToText(GetCustomers newVal) {
		try {
			txtId.setText(newVal.getId());
			String[] nameParts = newVal.getName().split("\\.", 2);
			if (nameParts.length > 1) {
				comboTitle.setValue(nameParts[0] + ".");
				txtName.setText(nameParts[1].trim());
			} else {
				txtName.setText(newVal.getName());
			}

			txtDob.setValue(LocalDate.parse(newVal.getDob()));
			txtSalary.setText("" + newVal.getSalary());
			txtAddress.setText(newVal.getAddress());
			txtCity.setText(newVal.getCity());
			txtProvince.setText(newVal.getProvince());
			txtPostalCode.setText(newVal.getPostalCode());

		} catch (NullPointerException e) {
			System.out.println("Add Value To Text encountered an issue: " + e);
		}
	}


	@FXML
	void btnClearOnAction(ActionEvent event) {
		clearText();
	}

	void clearText() {
		txtId.setText("");
		comboTitle.setValue("");
		txtName.setText("");
		txtDob.setValue(null);
		txtSalary.setText("");
		txtAddress.setText("");
		txtCity.setText("");
		txtProvince.setText("");
		txtPostalCode.setText("");

	}




}



