package model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	private String orderId;
	private LocalDate orderDate;
	private String custId;
	private ArrayList<OrderDetail> orderDetails;

}
