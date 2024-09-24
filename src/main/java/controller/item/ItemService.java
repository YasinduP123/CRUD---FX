package controller.item;

import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;
import model.SubItem;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemService {
	boolean addItem(Item item);

	ObservableList<Item> getItems();

	boolean updateItem(Item item);

	boolean deleteItem(String id);

	Item searchItem(String id);
	ObservableList<String> getItemCode();

	boolean updateStock(ArrayList<OrderDetail> orderDetails);

	ObservableList<Item> getOrderItems(String item) throws SQLException;

}
