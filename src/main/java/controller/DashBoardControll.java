package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardControll {

	@FXML
	void btnAddCustomerFormOnAction(ActionEvent event) {
		Stage stage = new Stage();
		try {
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/add_customer_form.fxml"))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		stage.show();
	}

	@FXML
	void btnViewCustomerFormOnAction(ActionEvent event) {
		Stage stage = new Stage();
		try {
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/view_order_details_form.fxml"))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		stage.show();
	}

	public void btnAddItemOnAction(ActionEvent actionEvent) {
		Stage stage = new Stage();
		try {
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/add_item_form.fxml"))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		stage.show();
	}


	@FXML
	public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
		Stage stage = new Stage();
		try {
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/place_order_form.fxml"))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		stage.show();
	}
}
