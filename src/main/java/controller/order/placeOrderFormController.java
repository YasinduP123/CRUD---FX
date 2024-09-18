package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.Customer.CustomerController;
import controller.item.ItemController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class placeOrderFormController implements Initializable {
	@FXML
	public JFXTextField txtUnitPrice;

	@FXML
	public TextField txtOrderId;

	@FXML
	private TableColumn<?, ?> colDescription;

	@FXML
	private TableColumn<?, ?> colItemCode;

	@FXML
	private TableColumn<?, ?> colQty;

	@FXML
	private TableColumn<?, ?> colTotal;

	@FXML
	private TableColumn<?, ?> colUnitPrice;

	@FXML
	private JFXComboBox<String> comboCustomerId;

	@FXML
	private JFXComboBox<String> comboItemCode;

	@FXML
	private Label lblNetTotal;

	@FXML
	private Label lblOrderDate;

	@FXML
	private Label lblOrderId;

	@FXML
	private Label lblOrderTime;

	@FXML
	private TableView<CartTm> tblPlaceOrder;

	@FXML
	private JFXTextField txtCustomerAddress;

	@FXML
	private JFXTextField txtCustomerName;

	@FXML
	private JFXTextField txtDescription;

	@FXML
	private JFXTextField txtQty;

	@FXML
	private JFXTextField txtStock;

	ObservableList<CartTm> cartTms = FXCollections.observableArrayList();

	private Integer addCartQty = 0;

	private Double netTotal = 0.00;

	Customer customer;

	Item item;
	Integer itemStoke;
	CartTm cartTm;
	String itemCodeNewVal;
	@FXML
	void btnAddToCardOnAction(ActionEvent event) {

		try{
			itemStoke = Integer.parseInt(txtStock.getText());

			if(itemStoke < Integer.parseInt(txtQty.getText())){
				new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
			}else {
				cartTm = new CartTm(
						comboItemCode.getValue(),
						txtDescription.getText(),
						Integer.parseInt(txtQty.getText()),
						Double.parseDouble(txtUnitPrice.getText()),
						Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtUnitPrice.getText())
				);



				colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
				colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
				colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
				colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
				colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

				tblPlaceOrder.setItems(cartTms);

				calculate();

				if (netTotal >= 0.0) {
					lblNetTotal.setText(""+netTotal);
				}

			}

		}catch (NumberFormatException e){
			new Alert(Alert.AlertType.ERROR,"Cannot enter null values !").show();
		}

		loadItemCode();
		cartTms.addAll(cartTm);
	}

	private void loadDateAndTime(){
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		lblOrderDate.setText(f.format(date));

		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime now = LocalTime.now();
			lblOrderTime.setText(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
		}),
				new KeyFrame(Duration.seconds(1))
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private void loadCustomerIds(){
		ObservableList<String> observableList = FXCollections.observableArrayList();
		List<String> customerIds = CustomerController.getInstance().getCustomerIds();
		observableList.addAll(customerIds);

		comboCustomerId.setItems(observableList);
	}

	private void loadItemCode(){
		ObservableList<String> itemCode = ItemController.getInstance().getItemCode();

		comboItemCode.setItems(itemCode);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		loadDateAndTime();
		loadCustomerIds();
		loadItemCode();

		comboCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
			System.out.println(newVal);
			if(newVal!=null){
				searchCustomer(newVal);
			}

		});

		comboItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
			if(newVal!=null){
				searchItem(newVal);
				this.itemCodeNewVal = newVal;

			}
		});

	}

	private void searchCustomer(String id){
		customer = CustomerController.getInstance().searchCustomer(id);

		txtCustomerName.setText(customer.getName());
		txtCustomerAddress.setText(customer.getAddress());

	}

	private void searchItem(String code){

		item = ItemController.getInstance().searchItem(code);
		txtDescription.setText(item.getDescription());
		txtStock.setText(""+item.getQtyOnHand());
		txtUnitPrice.setText(item.getUnitPrice());
	}

	@FXML
	void btnClearTableOnAction(ActionEvent event) {
		for ( int i = 0; i<tblPlaceOrder.getItems().size(); i++) {
			tblPlaceOrder.getItems().clear();
		}
		lblNetTotal.setText("0000");
		netTotal = 0.00;
	}

	@FXML
	void btnRemoveRowOnAction(ActionEvent event) {
		CartTm selectedItem = tblPlaceOrder.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			netTotal = netTotal-selectedItem.getTotal();
			lblNetTotal.setText(""+(netTotal));
			tblPlaceOrder.getItems().remove(selectedItem);
		}
		loadItemCode();
	}

	private void calculate(){
		netTotal += Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtUnitPrice.getText());
	}


	@FXML
	void btnPlaceOrderOnAction(ActionEvent event) {
		String orderId = txtOrderId.getText();
		LocalDate date = LocalDate.parse(lblOrderDate.getText());
		String customerId = comboCustomerId.getValue();
		ArrayList<OrderDetail> orderDetails = new ArrayList<>();

		cartTms.forEach(obj->{
			orderDetails.add(
					new OrderDetail(
							txtOrderId.getText(),
							obj.getItemCode(),
							obj.getQty(),
							0.0
					)
			);
		});

		Order order = new Order(orderId, date, customerId, orderDetails);
		System.out.println(order);
		try {
			new OrderController().placeOrder(order);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void btnCustomerFormOnAction(ActionEvent actionEvent) {
		Stage stage = new Stage();
		try {
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/add_customer_form.fxml"))));
			stage.show();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
