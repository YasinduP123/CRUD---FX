package controller.order;

import controller.util.CrudUtil;
import model.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailController {
	public boolean addOrderDetail(ArrayList<OrderDetail> orderDetails) {
		for(OrderDetail orderDetail : orderDetails){
			boolean isAddOrderDetail = addOrderDetail(orderDetail);
			if (!isAddOrderDetail){
				return false;
			}
		}
		return true;
	}

	public boolean addOrderDetail(OrderDetail orderDetails){
		String SQL = "Insert into orderdetail values (?,?,?,?)";
		try {
			return CrudUtil.execute(SQL,
				orderDetails.getOrderId(),
				orderDetails.getItemCode(),
				orderDetails.getOrderQty(),
				orderDetails.getDiscount()
			);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
