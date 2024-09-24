package controller.item;

import controller.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ItemController implements ItemService {

	private static ItemController instance;

	private ItemController (){

	}

	public static ItemController getInstance(){
		return instance == null ? new ItemController() : instance;
	}
	@Override
	public boolean addItem(Item item) {
		try {
			return CrudUtil.execute("insert into item values (?,?,?,?,?)",
					item.getItemCode(),
					item.getDescription(),
					item.getPackSize(),
					item.getUnitPrice(),
					item.getQtyOnHand()
			);


		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ObservableList<Item> getItems() {
		return null;
	}

	@Override
	public boolean updateItem(Item item) {
		try {
			return CrudUtil.execute("Update item set Description=? , PackSize=? , UnitPrice=? , QtyOnHand=? where ItemCode=?",
					item.getDescription(),
					item.getPackSize(),
					item.getUnitPrice(),
					item.getQtyOnHand(),
					item.getItemCode()
					);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean deleteItem(String id) {
		try {
			return CrudUtil.execute("delete from item where ItemCode=?", id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Item searchItem(String id) {

		try {
			ResultSet rst = CrudUtil.execute("select * from item where itemcode = ?", id);
			while (rst.next()){
				return new Item(
				rst.getString(1),
				rst.getString(2),
				rst.getString(3),
				rst.getString(4),
				rst.getInt(5)
				);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


		return null;
	}

	@Override
	public ObservableList<String> getItemCode(){
		ObservableList<String> observableList = FXCollections.observableArrayList();
		try {
			ResultSet rst = CrudUtil.execute("Select ItemCode from item");
			while (rst.next()){
				observableList.add(rst.getString(1));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return observableList;
	}

	@Override
	public boolean updateStock(ArrayList<OrderDetail> orderDetails) {
		for (OrderDetail orderDetail : orderDetails){
			boolean isUpdateStock = updateStock(orderDetail);
			if (!isUpdateStock){
				return false;
			}
		}
		return true;
	}

	@Override
	public ObservableList<Item> getOrderItems(String itemCode) {
		ObservableList<Item> orderItems = FXCollections.observableArrayList();

		try {
			String SQL = "select *  from Item where ItemCode=?";
			ResultSet rst = CrudUtil.execute(SQL, itemCode);
			while (rst.next()){
				orderItems.add(
						new Item(
								rst.getString(1),
								rst.getString(2),
								rst.getString(3),
								rst.getString(4),
								rst.getInt(5)
						)
				);
//
//				System.out.println(rst.getString(1));
//				System.out.println(rst.getString(2));
//				System.out.println(rst.getString(3));
//				System.out.println(rst.getString(4));
//				System.out.println(rst.getInt(5));
//				System.out.println("==========================================\n");

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return orderItems;
	}

	public boolean updateStock(OrderDetail orderDetails) {

		String SQL = "Update Item set qtyOnHand = qtyOnHand - ? where ItemCode = ? ";
		try {

		 return CrudUtil.execute(SQL , orderDetails.getOrderQty() , orderDetails.getItemCode());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
