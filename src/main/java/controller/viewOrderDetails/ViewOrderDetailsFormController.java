package controller.viewOrderDetails;

import com.jfoenix.controls.JFXComboBox;
import controller.Customer.CustomerController;
import controller.item.ItemController;
import controller.order.OrderController;
import controller.order.OrderDetailController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewOrderDetailsFormController implements Initializable {

	@FXML
	public TableColumn<?, ?> colPackSize;

	@FXML
	public Label lblCustomerId;

	@FXML
	public Label lblCustomerName;

	@FXML
	private TableColumn<?, ?> colItemCode;

	@FXML
	private TableColumn<?, ?> colItemDescription;

	@FXML
	private TableColumn<?, ?> colQty;

	@FXML
	private TableColumn<?, ?> colUnitPrice;

	@FXML
	private JFXComboBox<String> comboOrderId;

	@FXML
	private Label lblNetTotal;

	@FXML
	private Label lblOrderDate;

	@FXML
	private TableView<ViewOrderDetailService> tblOrderView;

	Integer orderQty;

	SubOrderDetail getSubOrderDetail;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		try {
			ObservableList<String> orderIdList = OrderController.getInstance().loadOrderId();
			comboOrderId.setItems(orderIdList);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		comboOrderId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
			setDataToOrderDetailTable(newVal);
			getOrderDate(newVal);
			viewCustomerId(newVal);
		});

	}

	private void setDataToOrderDetailTable(String orderId) {
		try {
			List<SubOrderDetail> orderDetails = new OrderDetailController().getOrderDetails(orderId);
			ObservableList<ViewOrderDetailService> allDetails = FXCollections.observableArrayList();

			getNetTotal(orderDetails,allDetails);
			viewCustomerId(orderId);

			colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
			colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
			colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
			colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
			colQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));

			tblOrderView.setItems(allDetails);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void getOrderDate(String orderId) {
		lblOrderDate.setText((OrderController.getInstance().getOrderDate(orderId)).getOrderDate().toString());
	}

	private void getNetTotal(List<SubOrderDetail> orderDetails ,ObservableList<ViewOrderDetailService> allDetails) {

		double netTotal = 0.0;

		for (SubOrderDetail subOrderDetail : orderDetails) {
			ObservableList<Item> orderItems = ItemController.getInstance().getOrderItems(subOrderDetail.getItemCode());

			for (Item item : orderItems) {
				ViewOrderDetailService detailService = new ViewOrderDetailService(
						item.getItemCode(),
						item.getDescription(),
						item.getPackSize(),
						item.getUnitPrice(),
						subOrderDetail.getOrderQty()
				);

				allDetails.add(detailService);

				Double unitPrice = Double.parseDouble(item.getUnitPrice());
				Integer orderQty = subOrderDetail.getOrderQty();
				netTotal += (unitPrice * orderQty);
			}

		}

		String netTotalStr = String.format("%,.2f", netTotal);
		lblNetTotal.setText(netTotalStr);
		System.out.println(allDetails);

	}

	public void viewCustomerId(String orderId){
		String custId = (OrderController.getInstance().getOrderDate(orderId)).getCustId();
		lblCustomerId.setText(custId);
		viewCustomerName(custId);
	}

	public void viewCustomerName(String custId){
		Customer customer = CustomerController.getInstance().searchCustomer(custId);
		lblCustomerName.setText(customer.getName());

	}



}
