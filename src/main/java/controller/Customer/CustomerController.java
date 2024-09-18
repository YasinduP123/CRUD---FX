package controller.Customer;

import controller.util.CrudUtil;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerService {
	private static CustomerController instance;
	private CustomerController(){

	}

	public static CustomerController getInstance(){
		return instance == null ? new CustomerController() : instance;
	}
	@Override
	public boolean addCustomer(Customer customer) {
		try {
			return CrudUtil.execute(
					"Insert into customer values (?,?,?,?,?,?,?,?,?)",
					customer.getId(),
					customer.getTitle(),
					customer.getName(),
					customer.getDob(),
					customer.getSalary(),
					customer.getAddress(),
					customer.getCity(),
					customer.getProvince(),
					customer.getPostalCode()
			);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ObservableList<Customer> getCustomers() {
		ObservableList<Customer> observableList = FXCollections.observableArrayList();
		try {
			ResultSet rst = CrudUtil.execute("Select * from customer");
			while (rst.next()) {
				observableList.add(
					new Customer(
							rst.getString(1),
							rst.getString(2),
							rst.getString(3),
							rst.getDate(4).toLocalDate(),
							rst.getDouble(5),
							rst.getString(6),
							rst.getString(7),
							rst.getString(8),
							rst.getString(9)
					)
				);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return observableList ;
	}

	@Override
	public boolean updateCustomer(Customer customer) {
		try {
			return CrudUtil.execute("update customer set CustTitle=?, CustName=? , DOB=?,  Salary=? , CustAddress=? , City=? , Province=? , PostalCode=? where CustId=?",
					customer.getTitle(),
					customer.getName(),
					customer.getDob(),
					customer.getSalary(),
					customer.getAddress(),
					customer.getCity(),
					customer.getProvince(),
					customer.getPostalCode(),
					customer.getId()
			);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<String> getCustomerIds(){
		ObservableList<Customer> allCustomers = getCustomers();
		ArrayList<String> custIdList = new ArrayList<>();

		allCustomers.forEach(obj->{
			custIdList.add(obj.getId());
		});

		return custIdList;
	}

	@Override
	public boolean deleteCustomer(String id) {
		try {
			return CrudUtil.execute("Delete from customer where CustId = ?", id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Customer searchCustomer(String id) {

		try {
			ResultSet rst = CrudUtil.execute("Select * from customer where CustID=?", id);

			while (rst.next()){
				 return new Customer(
						rst.getString(1),
						rst.getString(2),
						rst.getString(3),
						rst.getDate(4).toLocalDate(),
						rst.getDouble(5),
						rst.getString(6),
						rst.getString(7),
						rst.getString(8),
						rst.getString(9)
				);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return null;
	}
}
