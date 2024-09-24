package controller.order;

import controller.util.CrudUtil;
import model.OrderDetail;
import model.SubOrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public List<SubOrderDetail> getOrderDetails(String orderId) throws SQLException {
		List<SubOrderDetail> orderDetails = new ArrayList<>();
		String SQL = "Select * from orderdetail where orderId=?";

		ResultSet rst = CrudUtil.execute(SQL,orderId);

		while (rst.next()){
			orderDetails.add(
					new SubOrderDetail(
							rst.getString(2),
							rst.getInt(3)
					)
			);
		}

		return orderDetails;
	}


}
