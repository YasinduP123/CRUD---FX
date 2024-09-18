package model;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
	private String orderId;
	private String itemCode;
	private Integer orderQty;
	private Double discount;

}
