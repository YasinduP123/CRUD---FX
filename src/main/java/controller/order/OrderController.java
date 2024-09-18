package controller.order;

import controller.item.ItemController;
import db.DBConnection;
import javafx.scene.control.Alert;
import model.Order;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderController {
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
			return false;

		}finally {
			connection.rollback();
			connection.setAutoCommit(true);
		}
	}
}
