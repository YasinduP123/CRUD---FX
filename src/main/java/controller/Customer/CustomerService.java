package controller.Customer;

import javafx.collections.ObservableList;
import model.Customer;

import java.util.List;

public interface CustomerService {
	boolean addCustomer(Customer customer);

	ObservableList<Customer> getCustomers();

	boolean updateCustomer(Customer customer);

	boolean deleteCustomer(String id);

	Customer searchCustomer(String id);

	List<String> getCustomerIds();

}
