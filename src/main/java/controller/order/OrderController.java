package controller.order;

import controller.item.ItemController;
import controller.util.CrudUtil;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Order;
import model.OrderDetail;
import model.SubOrder;

import java.sql.*;
import java.time.LocalDate;

public class OrderController {

	private static OrderController instance;
	private OrderController(){

	}

	public boolean placeOrder(Order order) throws SQLException {
		Connection connection = DBConnection.getInstance().getConnection();
		try {
			connection.setAutoCommit(false);
			PreparedStatement psTm = connection.prepareStatement("Insert Into orders values (?,?,?)");
			psTm.setObject(1,order.getOrderId());
			psTm.setObject(2,order.getOrderDate());
			psTm.setObject(3,order.getCustId());
			boolean isOrderAdd = psTm.executeUpdate()>0;

			if (isOrderAdd){
				boolean orderDetailsAdd = new OrderDetailController().addOrderDetail(order.getOrderDetails());
				if (orderDetailsAdd){
					boolean updateStock = ItemController.getInstance().updateStock(order.getOrderDetails());
					if (updateStock){
						new Alert(Alert.AlertType.CONFIRMATION,"Order Placed !").show();
						connection.commit();
						return true;
					}
				}
			}

			new Alert(Alert.AlertType.ERROR , "Order NOT Placed :(").show();
			connection.rollback();
			return false;

		}finally {
			connection.setAutoCommit(true);
		}
	}

	public static OrderController getInstance() {
		if (instance == null){
			return instance = new OrderController();
		}
		return instance;
	}

	public ObservableList<String> loadOrderId() throws SQLException {
		ObservableList<String> orderIdList = FXCollections.observableArrayList();
		String SQL = "select OrderId from orders";

		ResultSet rst = CrudUtil.execute(SQL);
		while (rst.next()){
			orderIdList.add(rst.getString(1));
		}

		return orderIdList;

	}


	public SubOrder getOrderDate(String orderId) {
		LocalDate date = null;
		String custName = null;
		SubOrder subOrder = null;

		String SQL = "select * from orders where orderId=?";
		try {
			ResultSet resultSet = CrudUtil.execute(SQL, orderId);
			while (resultSet.next()){
				date = LocalDate.parse(resultSet.getString(2));
				custName = resultSet.getString(3);

				subOrder = new SubOrder(orderId,date,custName);

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return subOrder;
	}
}
