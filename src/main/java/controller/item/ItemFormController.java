package controller.item;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

	@FXML
	private TableColumn<?, ?> colItemCode;

	@FXML
	private TableColumn<?, ?> colDescription;

	@FXML
	private TableColumn<?, ?> colPackSize;

	@FXML
	private TableColumn<?, ?> colQty;

	@FXML
	private TableColumn<?, ?> colUnitPrice;

	@FXML
	private TableView<Item> itemTable;

	@FXML
	private JFXTextField txtDescription;

	@FXML
	private JFXTextField txtItemCode;

	@FXML
	private JFXTextField txtPackSize;

	@FXML
	private JFXTextField txtQtyOnHand;

	@FXML
	private JFXTextField txtUnitPrice;

	ItemService service = ItemController.getInstance();


	@FXML
	void btnAddItemOnAction(ActionEvent event) {
		Item item = new Item(
				txtItemCode.getText(),
				txtDescription.getText(),
				txtPackSize.getText(),
				txtUnitPrice.getText(),
				Integer.parseInt(txtQtyOnHand.getText())
		);

		if (service.addItem(item)) {
			new Alert(Alert.AlertType.CONFIRMATION, "Add success!").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Add not success!").show();
		}
		loadTable();
	}

	@FXML
	void btnClearItemOnAction(ActionEvent event) {
		txtItemCode.setText("");
		txtDescription.setText("");
		txtPackSize.setText("");
		txtUnitPrice.setText("");
		txtQtyOnHand.setText("");
	}

	@FXML
	void btnDeleteItemOnAction(ActionEvent event) {
		new Item(
				txtItemCode.getText(),
				txtDescription.getText(),
				txtPackSize.getText(),
				txtUnitPrice.getText(),
				Integer.parseInt(txtQtyOnHand.getText())
		);

		if (service.deleteItem(txtItemCode.getText())) {
			new Alert(Alert.AlertType.CONFIRMATION, "Delete success!").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Delete not success!").show();
		}
		loadTable();
	}

	@FXML
	void btnSearchItemOnAction(ActionEvent event) {

	}

	@FXML
	void btnUpdateItemOnAction(ActionEvent event) {
		Item item = new Item(
				txtItemCode.getText(),
				txtDescription.getText(),
				txtPackSize.getText(),
				txtUnitPrice.getText(),
				Integer.parseInt(txtQtyOnHand.getText())
		);

		if (service.updateItem(item)) {
			new Alert(Alert.AlertType.CONFIRMATION, "Update success!").show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Update not success!").show();
		}
		loadTable();
	}

	@FXML
	public void btnReloadOnAction(ActionEvent actionEvent) {
		loadTable();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		loadTable();

		itemTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
			try {
				if (newVal != null) {
					addValueToText(newVal);
				}
			} catch (NullPointerException e) {
				System.out.println(e);
			}

		});
	}

	private void loadTable() {
		ObservableList<Item> observableList = FXCollections.observableArrayList();

		colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
		colPackSize.setCellValueFactory(new PropertyValueFactory<>("PackSize"));
		colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("QtyOnHand"));
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement stm = connection.prepareStatement("select * from item");
			ResultSet rst = stm.executeQuery();

			while (rst.next()) {
				observableList.add(
						new Item(
							 	rst.getString("ItemCode"),
								rst.getString("Description"),
								rst.getString("PackSize"),
								rst.getString("UnitPrice"),
								rst.getInt("QtyOnHand")
						)
				);
			}
			itemTable.setItems(observableList);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void addValueToText(Item newVal) {
		txtItemCode.setText(newVal.getItemCode());
		txtDescription.setText(newVal.getDescription());
		txtPackSize.setText(newVal.getPackSize());
		txtUnitPrice.setText(newVal.getUnitPrice());
		txtQtyOnHand.setText("" + newVal.getQtyOnHand());
	}

}
