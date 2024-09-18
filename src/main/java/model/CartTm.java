package model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartTm {
	private String itemCode;
	private String description;
	private Integer qty;
	private Double unitPrice;
	private Double total;
}
